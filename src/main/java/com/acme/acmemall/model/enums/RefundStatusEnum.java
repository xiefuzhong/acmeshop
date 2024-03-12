package com.acme.acmemall.model.enums;

public enum RefundStatusEnum {
    //    退款状态 0未申请，1申请  2-审核通过 3-商品待收货 4-已退款 5-拒绝 6-取消申请 7完结 8失效 9 商品已收货
    REFUND_NO(0, "未申请"),
    REFUND_APPLY(1, "提交申请"),
    REFUND_PASS(2, "同意退款"),
    REFUND_RECEIVED(3, "退还商品"),
    REFUND_RETURNED(9, "退还入库"),
    REFUND_PAID(4, "已退款"),
    REFUND_REJECT(5, "拒绝退款"),
    REFUND_CANCEL(6, "撤销售后"),
    REFUND_END(7, "完结"),
    REFUND_EXPIRED(8, "失效");

    public int getCode() {
        return code;
    }

    private final int code;
    private final String name;

    public String getName() {
        return name;
    }

    RefundStatusEnum(int code, String name) {
        this.code = code;
        this.name = name;
    }


    public static RefundStatusEnum parse(Integer code) {
        if (code <= 0) {
            return REFUND_NO;
        }
        for (RefundStatusEnum status : values()) {
            if (code == status.getCode()) {
                return status;
            }
        }
        return REFUND_NO;
    }

    /**
     * 是否可以申请售后
     *
     * @param code
     * @return
     */
    public static Boolean canApply(int code) {
        RefundStatusEnum status = parse(code);
        if (status == REFUND_NO || status == REFUND_REJECT || status == REFUND_CANCEL) {
            return Boolean.TRUE;
        }
        return Boolean.FALSE;
    }

}
