package com.acme.acmemall.factory;

import com.acme.acmemall.model.OrderVo;

import java.util.Date;
import java.util.UUID;

/**
 * @description:
 * @author: ihpangzi
 * @time: 2023/2/22 14:37
 */
public class OrderFactory {

    public static OrderVo buildCartOrder(long userId) {
        return OrderVo.builder()
                .all_order_id(UUID.randomUUID().toString().replaceAll("-", ""))
                .add_time(new Date())
                .order_status(0)
                .pay_status(0)
                .user_id(userId)
                .build();
    }
}
