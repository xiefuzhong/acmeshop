package com.acme.acmemall.model;

import com.acme.acmemall.controller.reqeust.OrderRefundRequest;
import com.acme.acmemall.exception.Assert;
import com.acme.acmemall.model.enums.RefundStatusEnum;
import com.acme.acmemall.model.enums.RefundType;
import com.acme.acmemall.model.enums.ShipStatusEnum;
import com.acme.acmemall.utils.GsonUtil;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.apache.commons.lang.StringUtils;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Date;

/**
 * @description:
 * @author: ihpangzi
 * @time: 2024/2/28 19:47
 */
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
public class OrderRefundVo implements Serializable {

    @Builder.Default
    private long id = 0;
    private String order_id;
    private long user_id;
    // 1-仅退款 2-退货退款
    private Integer refund_type;
    private String refund_type_text;
    private String refund_reason;
    private String goods_info;
    private Integer refund_num;
    private String refund_explain;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "##0.00")
    private BigDecimal refund_price;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date add_time;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "##0.00")
    private BigDecimal refunded_price;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date refunded_time;
    private String refund_phone;
    private String refund_express;
    private String refund_express_name;
    private String refund_express_code;
    private String refuse_reason;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date refuse_time;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date confirm_time;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date cancel_time;

    private String remark;
    private Integer refund_status;
    private String refund_status_text;

    private String noreturn_reason; // 不需要退回的理由
    private Integer returnGoods; // 是否需要退回货物

    public String getRefund_status_text() {
        return RefundStatusEnum.parse(this.refund_status).getName();
    }

    //    RefundOptionEnum 操作
    private String refundOption;

    public BigDecimal getRefund_price() {
        return refund_price == null ? null : refund_price.setScale(2, RoundingMode.HALF_UP);
    }

    public BigDecimal getRefunded_price() {
        return refunded_price == null ? null : refunded_price.setScale(2, RoundingMode.HALF_UP);
    }

    /**
     * 提交申请
     *
     * @param userId
     */
    public void submit(long userId) {
        this.add_time = new Date();
        this.refund_status = RefundStatusEnum.REFUND_APPLY.getCode();
        this.user_id = userId;
    }

    public void updateRequest(OrderRefundRequest request) {
        this.refund_reason = request.getRefundReason();
        this.refund_express = request.getRefund_express();
        this.refund_express_name = request.getRefund_express_name();
        this.refund_phone = request.getRefund_phone();
        this.refuse_reason = request.getRefuse_reason();
        this.refunded_price = request.getRefundPrice();
        this.returnGoods = request.getReturnGoods();
        this.noreturn_reason = request.getNoreturn_reason();
    }

    /**
     * 商家审批
     */
    public void audit() {
        this.refund_status = RefundStatusEnum.REFUND_PASS.getCode();
        this.refunded_price = refund_price;
        this.refunded_time = new Date();
        // 退货退款 &审批选择了不退货 & 有理由
        if (RefundType.isRefundReturn(this.refund_type) && this.returnGoods == 0 && !StringUtils.isBlank(this.noreturn_reason)) {
            this.refund_type = RefundType.REFUND_ONLY.getCode();
        }
    }

    /**
     * 取消售后申请
     */
    public void cancel() {
        this.refund_status = RefundStatusEnum.REFUND_CANCEL.getCode();
        this.refund_explain = "用户自主取消申请售后";
        this.cancel_time = new Date();
    }

    /**
     * 拒绝退款
     */
    public void reject() {
        this.refund_status = RefundStatusEnum.REFUND_REJECT.getCode();
        this.refuse_time = new Date();
    }

    /**
     * 退回商品确认收货
     */
    public void confirm() {
        this.refund_status = RefundStatusEnum.REFUND_RETURNED.getCode();
        this.confirm_time = new Date();
    }

    /**
     * 已退款
     */
    public void refundPaid() {
        this.refund_status = RefundStatusEnum.REFUND_PAID.getCode();
        this.refunded_price = refund_price;
        this.refunded_time = new Date();
    }

    public void refundReturned() {
        Assert.isBlank(this.refund_express, "物流单号不能为空");
        Assert.isBlank(this.refund_phone, "联系人电话不能为空");
        this.refund_status = RefundStatusEnum.REFUND_RECEIVED.getCode();
    }

    public boolean canApply() {
        return RefundStatusEnum.canApply(this.refund_status);
    }

    public void resetRefundType(int shipping_status) {
        // 自动修复售后类型
        ShipStatusEnum shipStatus = ShipStatusEnum.parse(shipping_status);
        if (shipStatus == ShipStatusEnum.SHIP_NO) {
            this.refund_type = RefundType.REFUND_ONLY.getCode();
        } else if (shipStatus == ShipStatusEnum.SHIP_YES || shipStatus == ShipStatusEnum.SHIP_ROG) {
            this.refund_type = RefundType.REFUND_RETURN.getCode();
        }
    }

    @Override
    public String toString() {
        return GsonUtil.toJson(this);
    }
}
