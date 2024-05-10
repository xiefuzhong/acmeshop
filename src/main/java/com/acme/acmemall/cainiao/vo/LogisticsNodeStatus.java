package com.acme.acmemall.cainiao.vo;

public enum LogisticsNodeStatus {
    UNKNOWN("0", "揽收"),
    IN_TRANSIT("1", "在途"),
    DELIVERED("2", "已签收"),
    EXCEPTION("3", "异常"),
    PENDING("4", "待揽收"),
    PENDING_DELIVERY("5", "待派件"),
    PENDING_PICKUP("6", "待取件"),
    PENDING_SEND("7", "待发送"),
    PENDING_SEND_BACK("8", "待退回"),
    PENDING_SEND_CANCEL("9", "待取消"),
    PENDING_SEND_RETRY("10", "待重试"),
    PENDING_SEND_RETRY_CANCEL("11", "待取消重试"),
    PENDING_SEND_RETRY_SUCCESS("12", "待重试成功"),
    PENDING_SEND_RETRY_FAIL("13", "待重试失败"),
    PENDING_SEND_RETRY_FAIL_CANCEL("14", "待取消重试失败");

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
