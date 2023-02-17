package com.acme.acmemall.service.impl;

import com.acme.acmemall.dao.CouponMapper;
import com.acme.acmemall.model.CouponVo;
import com.acme.acmemall.service.ICouponService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * @description:优惠券
 * @author: ihpangzi
 * @time: 2023/2/13 12:02
 */
@Service
public class CouponService implements ICouponService {

    @Autowired
    CouponMapper mapper;

    /**
     * @param map
     * @return
     */
    @Override
    public List<CouponVo> queryCouponList(Map<String, Object> map) {
        return mapper.queryList(map);
    }

    /**
     * 按条件查询用户优惠券
     *
     * @param params
     * @return
     */
    @Override
    public List<CouponVo> queryUserCoupons(Map<String, Object> params) {
        return mapper.queryUserCoupons(params);
    }

    /**
     * 按条件查询用户优惠券
     *
     * @param id
     * @return
     */
    @Override
    public CouponVo getUserCoupon(Integer id) {
        return mapper.getUserCoupon(id);
    }

    /**
     * 按类型查询
     *
     * @param params
     * @return
     */
    @Override
    public CouponVo queryMaxUserEnableCoupon(Map<String, Object> params) {
        return mapper.queryMaxUserEnableCoupon(params);
    }

    /**
     * sendType = 1或4 的优惠券
     *
     * @param params
     * @return
     */
    @Override
    public List<CouponVo> queryUserCouponList(Map<String, Object> params) {
        return mapper.queryUserCouponList(params);
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
        return mapper.getValidUserCoupons(param);
    }

    /**
     * @param couponId
     * @return
     */
    @Override
    public CouponVo queryObject(Integer couponId) {
        return mapper.queryObject(couponId);
    }
}
