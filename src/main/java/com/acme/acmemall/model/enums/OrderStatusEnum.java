package com.acme.acmemall.model.enums;

/**
 * 订单状态
 */
public enum OrderStatusEnum {
    NEW(0, "待支付"),
    PAID(200, "已付款"),
    SHIPPED(300, "已发货"),
    ROG(301, "已收货"),
    CLOSED(500, "交易关闭"),
    COMPLETE(501, "交易完成"),
    CANCELED(101, "已取消"),
    DELETED(102, "已删除"),
    AFTER_SERVICE(400, "退款中");

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

    public static Boolean endCheck(int code) {
        OrderStatusEnum status = parse(code);
        if (status == OrderStatusEnum.CANCELED
                || status == OrderStatusEnum.CLOSED
                || status == OrderStatusEnum.COMPLETE
                || status == OrderStatusEnum.DELETED) {
            return Boolean.TRUE;
        }
        return Boolean.FALSE;
    }

}
