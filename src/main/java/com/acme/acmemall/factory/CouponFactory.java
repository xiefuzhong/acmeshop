package com.acme.acmemall.factory;

import com.acme.acmemall.model.CouponVo;
import com.acme.acmemall.utils.CharUtil;

import java.util.Date;

/**
 * @description:
 * @author: ihpangzi
 * @time: 2024/3/23 14:15
 */
public class CouponFactory {

    public static CouponVo buildCoupon(long userId) {
        return CouponVo.builder()
                .add_time(new Date())
                .user_id(userId)
                .coupon_number(CharUtil.getRandomString(12))
                .build();
    }
}
