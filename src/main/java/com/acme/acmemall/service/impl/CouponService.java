package com.acme.acmemall.service.impl;

import com.acme.acmemall.common.ResultMap;
import com.acme.acmemall.controller.reqeust.CouponRequest;
import com.acme.acmemall.dao.CouponMapper;
import com.acme.acmemall.dao.UserCouponMapper;
import com.acme.acmemall.exception.ResultCodeEnum;
import com.acme.acmemall.factory.CouponFactory;
import com.acme.acmemall.model.*;
import com.acme.acmemall.model.enums.ScopeEnum;
import com.acme.acmemall.service.ICouponService;
import com.acme.acmemall.utils.StringUtils;
import com.google.common.collect.Lists;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @description:优惠券
 * @author: ihpangzi
 * @time: 2023/2/13 12:02
 */
@Service
public class CouponService implements ICouponService {

    @Resource
    CouponMapper couponMapper;

    @Resource
    UserCouponMapper userCouponMapper;

    /**
     * @param map
     * @return
     */
    @Override
    public List<CouponVo> queryCouponList(Map<String, Object> map) {
        return couponMapper.queryList(map);
    }

    /**
     * 按条件查询用户优惠券
     *
     * @param params
     * @return
     */
    @Override
    public List<CouponVo> queryUserCoupons(Map<String, Object> params) {
        return couponMapper.queryUserCoupons(params);
    }

    /**
     * 按条件查询用户优惠券
     *
     * @param id
     * @return
     */
    @Override
    public CouponVo getUserCoupon(Integer id) {
        return couponMapper.getUserCoupon(id);
    }

    /**
     * 按类型查询
     *
     * @param params
     * @return
     */
    @Override
    public CouponVo queryMaxUserEnableCoupon(Map<String, Object> params) {
        return couponMapper.queryMaxUserEnableCoupon(params);
    }

    /**
     * sendType = 1或4 的优惠券
     *
     * @param params
     * @return
     */
    @Override
    public List<CouponVo> queryUserCouponList(Map<String, Object> params) {
        return couponMapper.queryUserCouponList(params);
    }

    /**
     * @param couponVo
     * @return
     */
    @Override
    public int updateUserCoupon(CouponVo couponVo) {
        return 0;
    }

    /**
     * @param param
     * @return
     */
    @Override
    public List<CouponVo> getValidUserCoupons(Map param) {
        String goodsIds = (String) param.get("goodsIds");
        param.remove("goodsIds");
        List<CouponVo> validCouponVos = couponMapper.getValidUserCoupons(param);
        // 按优惠券适用类型分组
        Map<Integer, List<CouponVo>> couponMap = validCouponVos.stream().collect(Collectors.groupingBy(CouponVo::getScope));
        List<CouponVo> coupons = couponMap.get(ScopeEnum.GOODS.getScope());
        if (CollectionUtils.isEmpty(coupons)) {
            return validCouponVos;
        }
        // 过滤商品券
        if (CollectionUtils.isEmpty(coupons)) {
            return validCouponVos;
        }
        List<CouponVo> resultList = couponMap.get(ScopeEnum.ALL.getScope());
        if (StringUtils.isNotEmpty(goodsIds)) {
            List<Long> goods_ids = Arrays.stream(goodsIds.split(",")).map(Long::parseLong).collect(Collectors.toList());
            param.clear();
            List<Long> coupon_ids = coupons.stream().map(CouponVo::getId).collect(Collectors.toList());
            param.put("goods_ids", goods_ids);
            param.put("coupon_ids", coupon_ids);
            List<GoodsCouponVo> goodsCouponList = couponMapper.selectGoodsCoupon(param);
            // 如果没有分组有，查询没有，要剔除
            if (CollectionUtils.isEmpty(goodsCouponList)) {
                return couponMap.get(ScopeEnum.ALL.getScope());
            }
            List<CouponVo> couponList = Lists.newArrayList();
            for (int i = 0; i < goodsCouponList.size(); i++) {
                GoodsCouponVo goodsCouponVo = goodsCouponList.get(i);
                for (int j = 0; j < coupons.size(); j++) {
                    if (goodsCouponVo.getCouponId() == coupons.get(j).getId()) {
                        couponList.add(coupons.get(j));
                    }
                }
            }
            resultList.addAll(couponList);
        }
        return resultList;
    }

    /**
     * @param couponId
     * @return
     */
    @Override
    public CouponVo queryObject(Long couponId) {
        return couponMapper.queryObject(couponId);
    }

    /**
     * @param couponRequest
     * @return
     */
    @Transactional(rollbackFor = {RuntimeException.class, Exception.class})
    @Override
    public ResultMap createCoupon(CouponRequest couponRequest, LoginUserVo userVo) {
        CouponVo couponVo = CouponFactory.buildCoupon(userVo.getUserId());
        couponVo.create(couponRequest);
        couponMapper.save(couponVo);
        if (CollectionUtils.isNotEmpty(couponRequest.getGoodsIds())) {
            List<GoodsCouponVo> goodsCoupons = couponRequest.getGoodsIds().stream().map(goodsId -> {
                GoodsCouponVo coupon = new GoodsCouponVo(couponVo.getId(), goodsId);
                coupon.setCouponId(couponVo.getId());
                coupon.setGoodsId(goodsId);
                return coupon;
            }).collect(Collectors.toList());
            couponMapper.batchSave(goodsCoupons);
        }
        return ResultMap.response(ResultCodeEnum.SUCCESS, couponVo);
    }


    @Transactional(rollbackFor = {RuntimeException.class, Exception.class})
    @Override
    public ResultMap updateCoupon(CouponVo couponVo, List<Long> userIds) {
        if (CollectionUtils.isEmpty(userIds)) {
            return ResultMap.error("请选择用户");
        }
        List<UserCouponVo> userCouponVoList = Lists.newArrayList();
        for (int i = 0; i < userIds.size(); i++) {
            UserCouponVo userCouponVo = UserCouponVo.builder()
                    .coupon_id(couponVo.getId())
                    .coupon_number(couponVo.getCoupon_number())
                    .order_id("0")
                    .user_id(userIds.get(i))
                    .coupon_price(couponVo.getType_money())
                    .merchantId(couponVo.getMerchantId())
                    .build();
            userCouponVo.receive(couponVo);
            userCouponVoList.add(userCouponVo);
        }
        userCouponMapper.saveBatch(userCouponVoList);
        couponVo.receive();
        couponMapper.update(couponVo);
        return ResultMap.ok();
    }
}
