package com.acme.acmemall.model.enums;

import org.apache.commons.lang.StringUtils;

public enum RefundOptionEnum {
    SUBMIT("submit", "提交申请"),
    AUDIT("audit", "同意退款"),
    REJECT("reject", "拒绝退款"),
    CANCEL("cancel", "取消申请"),
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
}
