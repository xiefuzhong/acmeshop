package com.acme.acmemall.common;

public enum GoodsStatus {
    IN_DRAFT(2, "草稿中"),
    ON_SALE(1, "在售"),
    NOT_ON_SALE(0, "暂不售卖");

    int statusCode;

    public String getStatusName() {
        return statusName;
    }

    String statusName;

    public int getStatusCode() {
        return statusCode;
    }

    private GoodsStatus(int statusCode, String statusName) {
        this.statusCode = statusCode;
        this.statusName = statusName;
    }

}
