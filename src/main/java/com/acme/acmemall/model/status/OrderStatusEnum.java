package com.acme.acmemall.model.status;

public enum OrderStatusEnum {
    NEW(0, "已提交"),
    PAID(201, "已付款"),
    SHIPPED(300, "已发货"),
    ROG(301, "已收货"),
    COMPLETE(500, "已完成"),
    CANCELLED(100, "已取消"),
    AFTER_SERVICE(400, "售后中");

    private final int code;
    private final String name;

    OrderStatusEnum(int code, String name) {
        this.code = code;
        this.name = name;
    }

    public int getCode() {
        return code;
    }

    public String getName() {
        return name;
    }

    /**
     * 状态解析
     *
     * @param code
     * @return
     */
    public static OrderStatusEnum parse(int code) {
        if (code <= 0) {
            return NEW;
        }
        for (OrderStatusEnum status : values()) {
            if (code == status.getCode()) {
                return status;
            }
        }
        return NEW;
    }

}
