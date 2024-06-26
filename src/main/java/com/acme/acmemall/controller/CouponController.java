package com.acme.acmemall.controller;

import com.acme.acmemall.annotation.IgnoreAuth;
import com.acme.acmemall.annotation.LoginUser;
import com.acme.acmemall.common.ResultMap;
import com.acme.acmemall.controller.reqeust.CouponRequest;
import com.acme.acmemall.exception.ResultCodeEnum;
import com.acme.acmemall.model.*;
import com.acme.acmemall.model.enums.CouponSendType;
import com.acme.acmemall.model.enums.ScopeEnum;
import com.acme.acmemall.service.*;
import com.acme.acmemall.utils.CharUtil;
import com.acme.acmemall.utils.StringUtils;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.jsoup.select.Evaluator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @description:优惠券服务
 * @author: ihpangzi
 * @time: 2023/2/17 9:45
 */
@Api(tags = "优惠券")
@RestController
@RequestMapping("/api/coupon")
public class CouponController extends ApiBase {

    @Resource
    IUserService userService;
    @Resource
    ICouponService couponService;
    @Resource
    IUserCouponService userCouponService;
    @Resource
    IProductService productService;
    @Resource
    IShopCartService cartService;

    /**
     * 获取客户优惠券列表
     */
    @ApiOperation(value = "获取优惠券列表")
    @GetMapping("/list")
    public Object list(@LoginUser LoginUserVo loginUser,
                       @RequestParam(value = "status", defaultValue = "0") Integer status) {
        Map param = Maps.newHashMap();
        param.put("user_id", loginUser.getUserId());
        param.put("coupon_status", status);
        List<CouponVo> couponVos = couponService.queryUserCoupons(param);
//        if (CollectionUtils.isNotEmpty(couponVos)) {
//            couponVos = couponVos.stream().map(couponVo -> {
//            }).collect(Collectors.toList());
//        }
        return toResponsSuccess(couponVos);
    }

    /**
     * 获取用户可用优惠券列表
     *
     * @param loginUser
     * @param merchantId
     * @param goodsTotalPrice
     * @return
     */
    @ApiOperation(value = "获取用户可用优惠券列表")
    @GetMapping("/valid-coupon-list")
    public Object getValidCouponList(@LoginUser LoginUserVo loginUser,
                                     Long merchantId,
                                     BigDecimal goodsTotalPrice,
                                     String goodsIds,
                                     Integer coupon_status) {
        Map param = Maps.newHashMap();
        param.put("user_id", loginUser.getUserId());
        param.put("merchantId", merchantId);
        param.put("goodsTotalPrice", goodsTotalPrice);
        param.put("goodsIds", goodsIds);
        List<CouponVo> validCouponVos = couponService.getValidUserCoupons(param);
        return toResponsSuccess(validCouponVos);
    }

    /**
     * 获取商户优惠券列表
     */
    @ApiOperation(value = "获取商户优惠券列表")
    @GetMapping("/listMer")
    @IgnoreAuth
    public Object listMer(@LoginUser LoginUserVo loginUser,
                          @RequestParam(value = "merchantId", defaultValue = "0") long merchantId,
                          @RequestParam(value = "send_type", defaultValue = "0") Integer send_type,
                          @RequestParam(value = "page") String page,
                          @RequestParam(value = "type", defaultValue = "0") Integer type) {
        Map param = Maps.newHashMap();

        param.put("merchantId", merchantId);
        if (type != null && type > 0) {
            param.put("type", type);
        }
        if (send_type != null && send_type > 0) {
            param.put("send_types", Lists.newArrayList(send_type));
        }
        List<CouponVo> couponVos = couponService.queryCouponList(param);
        if (org.apache.commons.lang.StringUtils.equalsIgnoreCase("goods", page)) {
            param.clear();
            param.put("user_id", loginUser.getUserId());
            List<UserCouponVo> userCouponList = userCouponService.queryUserCouponList(param);
            couponVos.forEach(couponVo -> couponVo.updateUsed_status(0));
            if (org.apache.commons.collections.CollectionUtils.isNotEmpty(userCouponList)) {
                Map<Long, String> userCouponMap = userCouponList.stream().collect(Collectors.toMap(UserCouponVo::getCoupon_id, UserCouponVo::getCoupon_number, (v1, v2) -> v1));
                couponVos.forEach(couponVo -> couponVo.updateUsed_status(userCouponMap.containsKey(couponVo.getId()) ? 1 : 0));
            }
        }

        return toResponsSuccess(couponVos);
    }

    /**
     * 根据商品获取可用优惠券列表
     */
    @ApiOperation(value = "根据商品获取可用优惠券列表")
    @GetMapping("/listByGoods")
    public Object listByGoods(@RequestParam(defaultValue = "cart") String type, @LoginUser LoginUserVo loginUser) {
        BigDecimal goodsTotalPrice = BigDecimal.ZERO;
        if (type.equals("cart")) {
            Map param = Maps.newHashMap();
            param.put("user_id", loginUser.getUserId());
            List<ShopCartVo> cartList = cartService.queryCartList(param);
            //获取购物车统计信息
            for (ShopCartVo cartItem : cartList) {
                if (null != cartItem.getChecked() && 1 == cartItem.getChecked()) {
                    goodsTotalPrice = goodsTotalPrice.add(cartItem.getRetail_price().multiply(new BigDecimal(cartItem.getNumber())));
                }
            }
        } else { // 是直接购买的
//            BuyGoodsVo goodsVo = (BuyGoodsVo) J2CacheUtils.get(J2CacheUtils.SHOP_CACHE_NAME, "goods" + loginUser.getUserId() + "");
            BuyGoodsVo goodsVo = null;
            ProductVo productInfo = productService.queryObject(goodsVo.getProductId());
            //商品总价
            goodsTotalPrice = productInfo.getRetail_price().multiply(new BigDecimal(goodsVo.getNumber()));
        }

        // 获取可用优惠券
        Map param = Maps.newHashMap();
        param.put("user_id", loginUser.getUserId());
        param.put("coupon_status", 1);
        List<CouponVo> couponVos = couponService.queryUserCoupons(param);
        List<CouponVo> useCoupons = new ArrayList<>();
        List<CouponVo> notUseCoupons = new ArrayList<>();
        for (CouponVo couponVo : couponVos) {
            if (goodsTotalPrice.compareTo(couponVo.getMin_goods_amount()) >= 0) { // 可用优惠券
                couponVo.setCoupon_status(1);
                useCoupons.add(couponVo);
            } else {
                couponVo.setCoupon_status(0);
                notUseCoupons.add(couponVo);
            }
        }
        useCoupons.addAll(notUseCoupons);
        return toResponsSuccess(useCoupons);
    }

    /**
     * 兑换优惠券
     */
    @ApiOperation(value = "兑换优惠券")
    @PostMapping("exchange")
    public Object exchange(@LoginUser LoginUserVo loginUser) {
        JSONObject jsonParam = getJsonRequest();
        String coupon_number = jsonParam.getString("coupon_number");
        if (StringUtils.isNullOrEmpty(coupon_number)) {
            return toResponsFail("当前优惠码无效");
        }
        //
        Map param = Maps.newHashMap();
        param.put("coupon_number", coupon_number);
        List<UserCouponVo> couponVos = userCouponService.queryUserCouponList(param);
        UserCouponVo userCouponVo = null;
        if (null == couponVos || couponVos.size() == 0) {
            return toResponsFail("当前优惠码无效");
        }
        userCouponVo = couponVos.get(0);
        if (null != userCouponVo.getUser_id() && !userCouponVo.getUser_id().equals(0L)) {
            return toResponsFail("当前优惠码已经兑换");
        }
        CouponVo couponVo = couponService.queryObject(userCouponVo.getCoupon_id());
        if (null == couponVo || null == couponVo.getUse_end_date() || couponVo.getUse_end_date().before(new Date())) {
            return toResponsFail("当前优惠码已经过期");
        }
        userCouponVo.exchange(loginUser.getUserId(), "0");
        userCouponService.update(userCouponVo);
        return toResponsSuccess(userCouponVo);
    }

    /**
     * 用户主动领取商户领取优惠券
     */
    @ApiOperation(value = "用户主动领取商户领取优惠券")
    @PostMapping("getMerCoupon")
    public Object getMerCoupon(@LoginUser LoginUserVo loginUser) {
        //1 用户优惠券信息中增加
        //2 优惠券表中数量减少
        JSONObject jsonParam = getJsonRequest();
        String id = jsonParam.getString("id");
        Map params = Maps.newHashMap();

        params.put("user_id", loginUser.getUserId());
        params.put("send_type", CouponSendType.SEND_TYPE_USER.getCode());
        params.put("id", id);
        List<CouponVo> couponVos = couponService.queryUserCoupons(params);
        if (null != couponVos && couponVos.size() > 0) {
            return toResponsFail("已经领取过，不能重复领取");
        }

        // 领取
        CouponVo couponVo = couponService.queryObject(Long.parseLong(id));
        if (couponVo == null) {
            return toResponsFail("领取失败");
        }
        if (couponVo.getSend_type() != 8) {
            return toResponsFail("该优惠券不支持手动领取");
        }
        //判断优惠券是否被领完
        params.clear();
        params.put("coupon_id", id);
        int count = userCouponService.queryUserGetTotal(params);
        if (couponVo.getTotalCount() <= count) {
            return toResponsFail("优惠券已领完");
        }
        return couponService.updateCoupon(couponVo, Lists.newArrayList(loginUser.getUserId()));
    }

    /**
     * 　　填写手机号码，领券
     */
    @ApiOperation(value = "领券优惠券")
    @PostMapping("newuser")
    public Object newuser(@LoginUser LoginUserVo loginUser) {
        JSONObject jsonParam = getJsonRequest();
        //
        String phone = jsonParam.getString("phone");
        String smscode = jsonParam.getString("smscode");
        // 校验短信码
//        SmsLogVo smsLogVo = userService.querySmsCodeByUserId(loginUser.getUserId());
//        if (null != smsLogVo && !smsLogVo.getSms_code().equals(smscode)) {
//            return toResponsFail("短信校验失败");
//        }
        // 更新手机号码
//        if (!StringUtils.isNullOrEmpty(phone)) {
//            if (phone.equals(loginUser.getMobile())) {
//                loginUser.setMobile(phone);
//                userService.update(loginUser);
//            }
//        }
        // 判断是否是新用户
        if (!StringUtils.isNullOrEmpty(loginUser.getMobile())) {
            return toResponsFail("当前优惠券只能新用户领取");
        }
        // 是否领取过了
        Map params = Maps.newHashMap();
        params.put("user_id", loginUser.getUserId());
        params.put("send_type", 4);
        List<CouponVo> couponVos = couponService.queryUserCoupons(params);
        if (null != couponVos && couponVos.size() > 0) {
            return toResponsFail("已经领取过，不能重复领取");
        }
        // 领取
        Map couponParam = Maps.newHashMap();
        couponParam.put("send_type", 4);
        CouponVo newCouponConfig = couponService.queryMaxUserEnableCoupon(couponParam);
        if (null != newCouponConfig) {
            UserCouponVo userCouponVo = UserCouponVo.builder()
                    .add_time(new Date())
                    .coupon_id(newCouponConfig.getId())
                    .coupon_number(CharUtil.getRandomString(12))
                    .user_id(loginUser.getUserId())
                    .coupon_price(newCouponConfig.getType_money())
                    .build();
            userCouponService.save(userCouponVo);
            return toResponsSuccess(userCouponVo);
        }
        return toResponsFail("领取失败");
    }

    //    @IgnoreAuth
    @GetMapping("/giveable")
    public Object getMerCouponList(@LoginUser LoginUserVo userVo,
                                   @RequestParam("merchantId") Long merchantId,
                                   @RequestParam("send_type") Integer sendType) {
        if (userVo == null) {
            return ResultMap.error(400, "非有效用户操作");
        }
        if (!userService.checkAdmin(userVo.getUserId())) {
            return ResultMap.response(ResultCodeEnum.UNAUTHORIZED);
        }
        Map param = Maps.newHashMap();
        param.put("merchantId", merchantId);
        // 按用户发放
        if (sendType == null) {
            sendType = 1;
        }
        param.put("send_types", Lists.newArrayList(sendType));
        List<CouponVo> couponVos = couponService.queryCouponList(param);
        return toResponsSuccess(couponVos);
    }

    @PostMapping("/hand-out")
    public Object SendCoupons(@LoginUser LoginUserVo userVo) {
        if (userVo == null) {
            return ResultMap.error(400, "非有效用户操作");
        }
        LoginUserVo loginUserVo = userService.queryByUserId(userVo.getUserId());
        if (loginUserVo == null || loginUserVo.getUserId() == 0) {
            return ResultMap.error(1001, "请先登录管理系统再操作!");
        }
        JSONObject request = getJsonRequest();
        String userIds = request.getString("userIds");
        String[] uids = userIds.split(",");
        List<Long> ids = Arrays.stream(uids).map(id -> Long.parseLong(id.trim())).collect(Collectors.toList());
        Long coupon_id = request.getLong("coupon_id");
        CouponVo couponVo = couponService.queryObject(coupon_id);
        if (couponVo == null) {
            return toResponsFail("优惠券不存在");
        }
        if (couponVo.getSend_type() != 1) {
            return toResponsFail("该优惠券不支持后台发放");
        }
        //判断优惠券是否被领完
        Map userParams = Maps.newHashMap();
        userParams.put("coupon_id", coupon_id);
        int count = userCouponService.queryUserGetTotal(userParams);
        if (couponVo.getTotalCount() <= count) {
            return toResponsFail("优惠券已领完");
        }
        return couponService.updateCoupon(couponVo, ids);
    }

    /**
     * 商家创建优惠券
     *
     * @param userVo
     * @return
     */
    @PostMapping("create")
    public Object createCoupons(@LoginUser LoginUserVo userVo) {
        if (userVo == null) {
            return ResultMap.response(ResultCodeEnum.USER_NOT_LOGIN);
        }
        if (!userService.checkAdmin(userVo.getUserId())) {
            return ResultMap.response(ResultCodeEnum.UNAUTHORIZED);
        }
        JSONObject request = getJsonRequest();
//        JSONArray array = request.getJSONArray("goodsIds");
//        List<Long> goodsIds = JSONArray.parseArray(array.toJSONString(), Long.class);
        CouponRequest couponRequest = JSONObject.toJavaObject(request, CouponRequest.class);
        logger.info(String.format(Locale.ROOT, "CouponRequest.toString==>%s", couponRequest.toString()));
        if (!couponRequest.checkRequest()) {
            return ResultMap.error(400, "优惠券参数错误");
        }
        return couponService.createCoupon(couponRequest, userVo);
    }
}
