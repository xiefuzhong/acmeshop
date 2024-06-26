package com.acme.acmemall.controller.reqeust;

import com.acme.acmemall.exception.Assert;
import com.acme.acmemall.utils.GsonUtil;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * @description:
 * @author: ihpangzi
 * @time: 2024/2/28 20:00
 */
@Data
public class OrderRefundRequest implements Serializable {
    private long userid;
    private int goodsCount;
    private String goodsInfo;
    private String orderId;
    private int refundType;
    private String refundReason;
    private String refundContent;
    private BigDecimal refundPrice;
    private String refund_express;
    private String refund_phone;
    private String refund_express_name;
    private String refund_express_code;
    private String refuse_reason;
    private String refundOption;
    private String noreturn_reason; // 不需要退回的理由
    private Integer returnGoods; // 是否需要退回货物

    public void reqCheck() {
        Assert.isNull(this.orderId, "订单号不能为空!");
        Assert.isNull(this.refundType, "退货申请类型不能为空!");
        Assert.isNull(this.refundReason, "退货申请原因不能为空!");
    }

    public void updateCheck() {
        Assert.isNull(orderId, "订单号不能为空!");
        Assert.isBlank(refundOption, "操作类型不能为空!");
    }

    @Override
    public String toString() {
        return GsonUtil.toJson(this);
    }
}
