package com.acme.acmemall.model.enums;

public enum RefundType {
    /**
     * 1-仅退款
     */
    REFUND_ONLY(1, "仅退款"),
    /**
     * 2-退货退款
     */
    REFUND_RETURN(2, "退货退款");

    private final Integer code;

    private final String title;

    RefundType(Integer code, String title) {
        this.code = code;
        this.title = title;
    }

    public Integer getCode() {
        return code;
    }

    public String getTitle() {
        return title;
    }

    public static RefundType parse(Integer code) {
        if (code == null || code <= 0) {
            return REFUND_ONLY;
        }

        for (RefundType type : RefundType.values()) {
            if (type.getCode().equals(code)) {
                return type;
            }
        }
        return REFUND_ONLY;
    }


    public static Boolean isRefundOnly(Integer code) {
        RefundType type = parse(code);
        return type == REFUND_ONLY;
    }

    public static Boolean isRefundReturn(Integer code) {
        RefundType type = parse(code);
        return type == REFUND_RETURN;
    }

}
