package com.acme.acmemall.model.enums;

import org.apache.commons.lang.StringUtils;

/**
 * 统计维度
 */
public enum Statisticsdimension {
    ORDER_PAYMENT_count("paidCount", "支付订单数量"),
    ORDER_REFUND_count("refundCount", "退款订单数量"),
    ORDER_PAYMENT("paid", "订单收款"),
    ORDER_REFUND("refund", "订单退款");
    private final String type;
    private final String title;

    public String getTitle() {
        return title;
    }

    public String getType() {
        return type;
    }

    Statisticsdimension(String type, String title) {
        this.type = type;
        this.title = title;
    }

    public static String getTitle(String type) {
        if (type == null) {
            return StringUtils.EMPTY;
        }
        for (Statisticsdimension item : values()) {
            if (item.getType().equals(type)) {
                return item.getTitle();
            }
        }
        return StringUtils.EMPTY;
    }
}

