package com.acme.acmemall.model.enums;

public enum PayType {
    /**
     * 微信支付
     */
    WECHAT(0, "微信支付"),
    /**
     * 支付宝
     */
    ALIPAY(1, "支付宝"),
    /**
     * 银行卡
     */
    BANK_CARD(2, "银行卡"),
    /**
     * 余额支付
     */
    BALANCE(3, "余额支付");

    private final Integer code;

    private final String title;

    public Integer getCode() {
        return code;
    }

    public String getTitle() {
        return title;
    }

    PayType(Integer code, String title) {
        this.code = code;
        this.title = title;
    }

    public static PayType parse(Integer code) {
        for (PayType type : values()) {
            if (type.getCode() == code) {
                return type;
            }
        }
        return null;
    }
}
