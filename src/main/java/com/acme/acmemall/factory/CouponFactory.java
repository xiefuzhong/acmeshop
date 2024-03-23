package com.acme.acmemall.factory;

import com.acme.acmemall.model.CouponVo;
import com.acme.acmemall.utils.SnowFlakeGenerateIdWorker;

import java.util.Date;
import java.util.Locale;

/**
 * @description:
 * @author: ihpangzi
 * @time: 2024/3/23 14:15
 */
public class CouponFactory {

    public static CouponVo buildCoupon(long userId) {
        String id = SnowFlakeGenerateIdWorker.generateId();
        return CouponVo.builder()
                .add_time(new Date())
                .user_id(userId)
                .coupon_number(String.format(Locale.ROOT, "%s", id))
                .build();
    }
}
