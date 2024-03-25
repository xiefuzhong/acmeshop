package com.acme.acmemall.model.enums;

public enum ConponSendType {

    SEND_TYPE_USER(8, "用户领取"),
    SEND_TYPE_MERCHANT(1, "商户赠送");

    private int code;
    private String title;

    public int getCode() {
        return code;
    }

    public String getTitle() {
        return title;
    }

    ConponSendType(int code, String title) {
        this.code = code;
        this.title = title;
    }

    /**
     * 优惠券类型
     *
     * @param code
     * @return
     */
    public static ConponSendType getByCode(int code) {
        for (ConponSendType type : ConponSendType.values()) {
            if (type.getCode() == code) {
                return type;
            }
        }
        return null;
    }

}
