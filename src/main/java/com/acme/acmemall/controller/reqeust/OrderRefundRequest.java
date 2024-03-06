package com.acme.acmemall.controller.reqeust;

import com.acme.acmemall.exception.Assert;
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
    private String refuse_reason;
    private String refundOption;
    public void check() {
        Assert.isNull(this.orderId, "订单号不能为空!");
        Assert.isNull(this.refundType, "退货申请类型不能为空!");
        Assert.isNull(this.refundReason, "退货申请原因不能为空!");
    }
}
