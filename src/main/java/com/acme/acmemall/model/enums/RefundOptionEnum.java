package com.acme.acmemall.model.enums;

import org.apache.commons.lang.StringUtils;

public enum RefundOptionEnum {
    SUBMIT("submit", "提交申请"),
    AUDIT("audit", "同意退款"),
    REJECT("reject", "拒绝退款"),
    CANCEL("cancel", "取消申请"),
    RECEIPT("receipt", "确认收货"),
    RETURN("return", "退还商品"),
    REFUND("refund", "立即退款"),
    LOGISTICS("fillLogistics", "填写退货物流");

    private final String code;
    private final String title;

    public String getCode() {
        return code;
    }

    public String getTitle() {
        return title;
    }

    RefundOptionEnum(String code, String title) {
        this.code = code;
        this.title = title;
    }

    public static RefundOptionEnum parse(String code) {
        if (StringUtils.isEmpty(code)) {
            return SUBMIT;
        }
        for (RefundOptionEnum status : values()) {
            if (StringUtils.equalsIgnoreCase(code, status.getCode())) {
                return status;
            }
        }
        return SUBMIT;
    }

    /**
     * 买家操作
     *
     * @param refundOption
     * @return
     */
    public static boolean buyerOption(String refundOption) {
        RefundOptionEnum option = parse(refundOption);
        if (option == SUBMIT || option == CANCEL || option == RETURN) {
            return Boolean.TRUE;
        }
        return Boolean.FALSE;
    }

    /**
     * 售后操作
     *
     * @param refundOption
     * @return
     */
    public static boolean merchantOption(String refundOption) {
        RefundOptionEnum option = parse(refundOption);
        if (option == AUDIT || option == REJECT || option == RECEIPT || option == REFUND) {
            return Boolean.TRUE;
        }
        return Boolean.FALSE;
    }
}
