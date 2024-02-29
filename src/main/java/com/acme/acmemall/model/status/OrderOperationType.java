package com.acme.acmemall.model.status;

public enum OrderOperationType {
    SUBMIT("提交"),
    PAY("支付"),
    SHIP("发货"),
    ROG("确认收货"),
    CANCEL("取消"),
    COMMENT("评价"),
    COMPLETE("完成"),
    SERVICE_CANCEL("取消订单");

    private final String name;

    OrderOperationType(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
