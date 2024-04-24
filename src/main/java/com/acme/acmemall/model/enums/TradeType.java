package com.acme.acmemall.model.enums;

public enum TradeType {
    /**
     * 订单收款
     */
    ORDER_PAYMENT(0, "订单收款"),
    /**
     * 订单退款
     */
    ORDER_REFUND(1, "订单退款");

    private final Integer type;
    private final String title;

    public Integer getType() {
        return type;
    }

    public String getTitle() {
        return title;
    }

    TradeType(Integer type, String title) {
        this.type = type;
        this.title = title;
    }

    public static TradeType parse(Integer type) {
        if (type == null) {
            return null;
        }
        for (TradeType tradeType : values()) {
            if (tradeType.getType() == type) {
                return tradeType;
            }
        }
        return null;
    }
}
