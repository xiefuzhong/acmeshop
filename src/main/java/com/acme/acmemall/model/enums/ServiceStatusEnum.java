package com.acme.acmemall.model.enums;

/**
 * 售后服务状态
 */
public enum ServiceStatusEnum {
    //    0未申请，1申请  2-审核通过 3-已退款 4-拒绝 5-取消申请 6完结 7失效
    EXPIRED(5, "已失效不允许申请售后"),
    CANCEL(3, "取消申请"),
    PASS(2, "审批通过"),
    REJECT(4, "拒绝"),
    APPLY_NO(0, "未申请"),
    APPLY_YES(1, "已申请"),
    PAID(6, "已退款"),
    CONFIRM_RECEIPT(7, "确认收货"),
    COMPLETE(8, "完成");

    private final Integer code;
    private final String title;

    ServiceStatusEnum(Integer code, String title) {
        this.code = code;
        this.title = title;
    }

    public Integer getCode() {
        return code;
    }

    public String getTitle() {
        return title;
    }

    public static ServiceStatusEnum parse(Integer code) {
        if (code <= 0) {
            return APPLY_NO;
        }
        for (ServiceStatusEnum status : values()) {
            if (code == status.getCode()) {
                return status;
            }
        }
        return APPLY_NO;
    }
}
