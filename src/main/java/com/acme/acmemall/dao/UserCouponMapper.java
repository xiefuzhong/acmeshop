package com.acme.acmemall.dao;

import com.acme.acmemall.model.UserCouponVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.Map;

/**
 * @description:用户优惠券
 * @author: ihpangzi
 * @time: 2023/2/17 10:51
 */
@Mapper
public interface UserCouponMapper extends BaseDao<UserCouponVo> {
    UserCouponVo queryByCouponNumber(@Param("coupon_number") String coupon_number);

    int queryUserGetTotal(Map userParams);

    void updateCouponStatus(UserCouponVo vo);
}
