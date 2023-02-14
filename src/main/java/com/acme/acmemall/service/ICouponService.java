package com.acme.acmemall.service;

import com.acme.acmemall.model.CouponVo;

import java.util.List;
import java.util.Map;

public interface ICouponService {
    List<CouponVo> queryCouponList(Map<String,Object> map);

    /**
     * 按条件查询用户优惠券
     *
     * @param params
     * @return
     */
    List<CouponVo> queryUserCoupons(Map<String, Object> params);

    /**
     * 按条件查询用户优惠券
     *
     * @param id
     * @return
     */
    CouponVo getUserCoupon(Integer id);

    /**
     * 按类型查询
     *
     * @param params
     * @return
     */
    CouponVo queryMaxUserEnableCoupon(Map<String, Object> params);

    /**
     * sendType = 1或4 的优惠券
     *
     * @param params
     * @return
     */
    List<CouponVo> queryUserCouponList(Map<String, Object> params);

    int updateUserCoupon(CouponVo couponVo);

    List<CouponVo> getValidUserCoupons(Map param);
}
