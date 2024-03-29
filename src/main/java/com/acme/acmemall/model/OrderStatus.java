//package com.acme.acmemall.model;
//
//public enum OrderStatus {
//    TO_BE_PAID(0, "待付款"),
//    /**
//     * 300 订单已发货
//     */
//    TO_BE_RECEIVED(300, "待收货"),
//    /**
//     * 301 用户确认收货
//     */
//    COMPLETED(301, "已完成"),
//    PAID(200, "已付款"),
//    CANCELED(101, "已取消"),
//    DELETED(102, "已删除"),
//    /**
//     * 401 没有发货，退款
//     */
//    CANCELED_RETURNED(401, "已取消"),
//    /**
//     * 402 已收货，退款退货
//     */
//    RETURNED(402, "已退货"),
//    TO_BE_SHIPPED(201, "待发货"); // 打单之后就是待发货，可以去填充快递信息发货，状态-待用户收货，物流状态：已发货
//
//    int code;
//    String description;
//
//    OrderStatus(int code, String description) {
//        this.code = code;
//        this.description = description;
//    }
//
//    /**
//     * 状态解析
//     *
//     * @param code
//     * @return
//     */
//    public static OrderStatus parse(int code) {
//        if (code <= 0) {
//            return TO_BE_PAID;
//        }
//        for (OrderStatus status : values()) {
//            if (code == status.getCode()) {
//                return status;
//            }
//        }
//        return TO_BE_PAID;
//    }
//
//    public static Boolean validCancle(int code) {
//        OrderStatus status = parse(code);
//        switch (status) {
//            case PAID:
//            case TO_BE_SHIPPED:
//            case TO_BE_PAID: {
//                return Boolean.TRUE;
//            }
//            default: {
//                return Boolean.FALSE;
//            }
//        }
//    }
//
//    public int getCode() {
//        return code;
//    }
//
//    public String getDescription() {
//        return description;
//    }
//}
