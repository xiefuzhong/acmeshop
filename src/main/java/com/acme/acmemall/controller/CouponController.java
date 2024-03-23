package com.acme.acmemall.controller;

import com.acme.acmemall.annotation.IgnoreAuth;
import com.acme.acmemall.annotation.LoginUser;
import com.acme.acmemall.common.ResultMap;
import com.acme.acmemall.controller.reqeust.CouponRequest;
import com.acme.acmemall.exception.ResultCodeEnum;
import com.acme.acmemall.model.*;
import com.acme.acmemall.service.*;
import com.acme.acmemall.utils.CharUtil;
import com.acme.acmemall.utils.StringUtils;
import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @description:优惠券服务
 * @author: ihpangzi
 * @time: 2023/2/17 9:45
 */
@Api(tags = "优惠券")
@RestController
@RequestMapping("/api/coupon")
public class CouponController extends ApiBase {

    @Autowired
    IUserService userService;
    @Autowired
    ICouponService couponService;
    @Autowired
    IUserCouponService userCouponService;
    @Autowired
    IProductService productService;
    @Autowired
    IShopCartService cartService;

    /**
     * 获取客户优惠券列表
     */
    @ApiOperation(value = "获取优惠券列表")
    @GetMapping("/list")
    public Object list(@LoginUser LoginUserVo loginUser) {
        Map param = Maps.newHashMap();
        param.put("user_id", loginUser.getUserId());
        List<CouponVo> couponVos = couponService.queryUserCoupons(param);
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
    public Object getValidCouponList(@LoginUser LoginUserVo loginUser, Long merchantId, BigDecimal goodsTotalPrice) {
        Map param = Maps.newHashMap();
        param.put("user_id", loginUser.getUserId());
        param.put("merchantId", merchantId);
        param.put("goodsTotalPrice", goodsTotalPrice);
        List<CouponVo> validCouponVos = couponService.getValidUserCoupons(param);
        return toResponsSuccess(validCouponVos);
    }

    /**
     * 获取商户优惠券列表
     */
    @ApiOperation(value = "获取商户优惠券列表")
    @PostMapping("/listMer")
    public Object listMer(@LoginUser LoginUserVo loginUser,
                          @RequestParam("merchantId") long merchantId,
                          @RequestParam("type") Integer type) {
        Map param = Maps.newHashMap();
        param.put("merchantId", merchantId);
        if (type != null && type > 0) {
            param.put("type", type);
        }
        List<CouponVo> couponVos = couponService.queryCouponList(param);
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
        userCouponVo.exchange(loginUser.getUserId());
        userCouponService.update(userCouponVo);
        return toResponsSuccess(userCouponVo);
    }

    /**
     * 用户主动领取商户领取优惠券
     */
    @IgnoreAuth
    @ApiOperation(value = "用户主动领取商户领取优惠券")
    @PostMapping("getMerCoupon")
    public Object getMerCoupon(@LoginUser LoginUserVo loginUser) {
        //1 用户优惠券信息中增加
        //2 优惠券表中数量减少
        JSONObject jsonParam = getJsonRequest();
        String id = jsonParam.getString("id");
        Map params = Maps.newHashMap();
        /*loginUser=new UserVo();
        loginUser.setUserId(new Long(13));*/
        params.put("user_id", loginUser.getUserId());
        params.put("send_type", 8);
        params.put("id", id);
        List<CouponVo> couponVos = couponService.queryUserCoupons(params);
        if (null != couponVos && couponVos.size() > 0) {
            return toResponsFail("已经领取过，不能重复领取");
        }

        // 领取
        Map couponParam = Maps.newHashMap();
        couponParam.put("send_type", 8);
        params.put("id", id);
        CouponVo newCouponConfig = couponService.queryObject(Integer.parseInt(id));
        //判断优惠券是否被领完
        Map userParams = Maps.newHashMap();
        userParams.put("coupon_id", id);
        int count = userCouponService.queryUserGetTotal(userParams);
        if (newCouponConfig.getTotalCount() <= count) {
            return toResponsFail("优惠券已领完");
        }
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
        } else {
            return toResponsFail("领取失败");
        }

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
    public Object getMerCouponList(@LoginUser LoginUserVo userVo, @RequestParam("send_type") Integer sendType) {
        if (userVo == null) {
            return ResultMap.error(400, "非有效用户操作");
        }
        LoginUserVo loginUserVo = userService.queryByUserId(userVo.getUserId());
        if (loginUserVo == null || loginUserVo.getUserId() == 0) {
            return ResultMap.error(1001, "请先登录管理系统再操作!");
        }
        Map param = Maps.newHashMap();
        param.put("merchantId", loginUserVo.getMerchantId());
        // 按用户发放
        if (sendType == null) {
            sendType = 1;
        }
        param.put("send_type", sendType);
        List<CouponVo> couponVos = couponService.queryCouponList(param);
        return toResponsSuccess(couponVos);
    }

    @PostMapping("/hand-out")
    public Object handOutCoupon(@LoginUser LoginUserVo userVo) {
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
        Integer coupon_id = request.getInteger("coupon_id");
        CouponVo couponVo = couponService.queryObject(coupon_id);
        //判断优惠券是否被领完
        Map userParams = Maps.newHashMap();
        userParams.put("coupon_id", coupon_id);
        int count = userCouponService.queryUserGetTotal(userParams);
        if (couponVo.getTotalCount() <= count) {
            return toResponsFail("优惠券已领完");
        }
        if (null != couponVo) {
            List<UserCouponVo> userCouponVoList = Lists.newArrayList();
            for (int i = 0; i < uids.length; i++) {
                UserCouponVo userCouponVo = UserCouponVo.builder()
                        .add_time(new Date())
                        .coupon_id(couponVo.getId())
                        .coupon_number(CharUtil.getRandomString(12))
                        .user_id(Long.parseLong(uids[i]))
                        .coupon_price(couponVo.getType_money())
                        .build();
                userCouponVoList.add(userCouponVo);
            }
            userCouponService.batchSave(userCouponVoList);
            return ResultMap.ok();
        }
        return ResultMap.error("发送失败");
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
        CouponRequest couponRequest = JSONObject.toJavaObject(request, CouponRequest.class);
        if (!couponRequest.checkRequest()) {
            return ResultMap.error(400, "优惠券参数错误");
        }
        return couponService.createCoupon(couponRequest, userVo);
    }
}
