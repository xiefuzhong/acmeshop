package com.acme.acmemall.service.impl;

import com.acme.acmemall.common.ResultMap;
import com.acme.acmemall.controller.reqeust.CouponRequest;
import com.acme.acmemall.dao.CouponMapper;
import com.acme.acmemall.dao.UserCouponMapper;
import com.acme.acmemall.exception.ResultCodeEnum;
import com.acme.acmemall.factory.CouponFactory;
import com.acme.acmemall.model.CouponVo;
import com.acme.acmemall.model.LoginUserVo;
import com.acme.acmemall.model.UserCouponVo;
import com.acme.acmemall.model.enums.CouponSendType;
import com.acme.acmemall.service.ICouponService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;
import java.util.Map;

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
        return couponMapper.getValidUserCoupons(param);
    }

    /**
     * @param couponId
     * @return
     */
    @Override
    public CouponVo queryObject(Integer couponId) {
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
        return ResultMap.response(ResultCodeEnum.SUCCESS, couponVo);
    }


    @Transactional(rollbackFor = {RuntimeException.class, Exception.class})
    @Override
    public ResultMap updateCoupon(CouponVo couponVo, LoginUserVo userVo) {
        UserCouponVo userCouponVo = UserCouponVo.builder()
                .add_time(new Date())
                .coupon_id(couponVo.getId())
                .coupon_number(couponVo.getCoupon_number())
                .order_id(0)
                .user_id(userVo.getUserId())
                .coupon_price(couponVo.getType_money())
                .build();
        if (CouponSendType.getByCode(couponVo.getSend_type()) == CouponSendType.SEND_TYPE_USER) {
            userCouponVo.receive(couponVo);
            couponVo.receive();
            userCouponMapper.updateCouponStatus(userCouponVo);
            couponMapper.update(couponVo);
            return ResultMap.ok();
        }
        return ResultMap.error("没有任何更新");
    }
}
