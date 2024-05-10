package com.acme.acmemall.cainiao.vo;

public enum LogisticsNodeStatus {
    NODE_STATUS_7("7", "揽收"),
    NODE_STATUS_7_1("7-1", "已揽收"),
    NODE_STATUS_7_2("7-2", "待揽收"),
    NODE_STATUS_8("8", "在途"),
    PENDING("8-2", "运输中"),
    PENDING_DELIVERY("8-5", "清关中"),
    PENDING_PICKUP("8-6", "到达转运中心"),
    PENDING_SEND("8-7", "干线"),
    PENDING_SEND_BACK("8-8", "转递"),
    PENDING_SEND_CANCEL("9", "派件中"),
    NODE_STATUS_9_1("9-1", "代收点"),
    NODE_STATUS_10("10", "已签收"),
    NODE_STATUS_10_1("10-1", "正常签收"),
    NODE_STATUS_10_3("10-3", "代收签收"),
    NODE_STATUS_10_4("10-4", "快递柜或驿站签收"),
    NODE_STATUS_10_5("10-5", "退签"),
    NODE_STATUS_12("12", "包裹异常"),
    NODE_STATUS_12_2("12-2", "超时未签收"),
    NODE_STATUS_12_3("12-3", "超时未更新"),
    NODE_STATUS_12_4("12-4", "派件异常"),
    NODE_STATUS_12_5("12-5", "退签（退货签收）"),
    NODE_STATUS_12_6("12-6", "快递柜或驿站超时未取"),
    NODE_STATUS_12_7("12-7", "退回件"),
    NODE_STATUS_12_9("12-9", "无法联系"),
    NODE_STATUS_12_10("12-10", "配送延时"),
    NODE_STATUS_12_11("12-11", "收货地址不详细"),
    NODE_STATUS_12_12("12-12", "收件人电话错误"),
    NODE_STATUS_12_13("12-13", "单号已拦截"),
    NODE_STATUS_12_14("12-14", "客户取消发货"),
    NODE_STATUS_12_15("12-15", "拒收"),
    NODE_STATUS_13("13", "转寄");


    private final String status;

    private final String statusDesc;

    public String getStatus() {
        return status;
    }

    public String getStatusDesc() {
        return statusDesc;
    }

    LogisticsNodeStatus(String status, String statusDesc) {
        this.status = status;
        this.statusDesc = statusDesc;
    }

    public static LogisticsNodeStatus parse(String status) {
        for (LogisticsNodeStatus statusEnum : LogisticsNodeStatus.values()) {
            if (statusEnum.getStatus().equals(status)) {
                return statusEnum;
            }
        }
        return null;
    }
}
