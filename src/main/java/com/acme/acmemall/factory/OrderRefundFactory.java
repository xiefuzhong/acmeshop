package com.acme.acmemall.factory;

import com.acme.acmemall.controller.reqeust.OrderRefundRequest;
import com.acme.acmemall.model.OrderRefundVo;

/**
 * @description:
 * @author: ihpangzi
 * @time: 2024/2/28 20:27
 */
public class OrderRefundFactory {

    public static OrderRefundVo build(OrderRefundRequest request, Long userId) {
        return OrderRefundVo.builder()
                .user_id(userId)
                .order_id(request.getOrderId())
                .refund_num(request.getGoodsCount())
                .refund_type(request.getRefundType())
                .goods_info(request.getGoodsInfo())
                .refund_price(request.getRefundPrice())
                .refund_reason(request.getRefundReason())
                .refund_explain(request.getRefundContent())
                .build();
    }
}
