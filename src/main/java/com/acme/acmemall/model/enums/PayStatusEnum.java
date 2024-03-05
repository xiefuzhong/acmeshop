package com.acme.acmemall.model.enums;

/**
 * 支付状态
 */
public enum PayStatusEnum {

    PAY_NO(0, "未付款"),
    PAY_ING(1, "支付中"),
    PAY_YES(2, "已付款"),
    PAY_REFUND(4, "已退款");

    private final int code;

    private final String name;


    public int getCode() {
        return code;
    }

    public String getName() {
        return name;
    }

    PayStatusEnum(int code, String name) {
        this.code = code;
        this.name = name;
    }

    public static PayStatusEnum parse(int code) {
        if (code <= 0) {
            return PAY_NO;
        }
        for (PayStatusEnum status : values()) {
            if (code == status.getCode()) {
                return status;
            }
        }
        return PAY_NO;
    }
}
