package com.acme.acmemall.model;

import com.acme.acmemall.controller.reqeust.OrderRefundRequest;
import com.acme.acmemall.model.enums.RefundStatusEnum;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;
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
    private Integer refund_type;
    private String refund_type_text;
    private String refund_reason;
    private String goods_info;
    private Integer refund_num;
    private String refund_explain;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "0.00")
    private BigDecimal refund_price;
    private Date add_time;


    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "0.00")
    private BigDecimal refunded_price;
    private Date refunded_time;
    private String refund_phone;
    private String refund_express;
    private String refund_express_name;
    private String refuse_reason;
    private String remark;
    private Integer refund_status;
    private String refund_status_text;

    private String refundOption;

    public String getRefund_status_text() {
        return RefundStatusEnum.parse(this.refund_status).getName();
    }


    public void submit(long userId) {
        this.add_time = new Date();
        this.refund_status = RefundStatusEnum.REFUND_APPLY.getCode();
        this.user_id = userId;
    }

    public void audit() {
        this.refund_status = RefundStatusEnum.REFUND_PASS.getCode();
        this.refunded_price = refund_price;
        this.refunded_time = new Date();
    }

    public void fillLogistics(OrderRefundRequest request) {
        this.refund_phone = request.getRefund_phone();
        this.refund_express = request.getRefund_express();
        this.refund_express_name = request.getRefund_express_name();
        this.refund_status = RefundStatusEnum.REFUND_RECEIVED.getCode(); // 已填写物流信息，待商家收货确认
    }

    public void cancel() {
        this.refund_status = RefundStatusEnum.REFUND_CANCEL.getCode();
        this.refund_explain = "用户自主取消申请售后";
    }

    public void reject(OrderRefundRequest request) {
        this.refund_status = RefundStatusEnum.REFUND_REJECT.getCode();
        this.refuse_reason = request.getRefuse_reason();
    }

    public void confirm() {
        this.refund_status = RefundStatusEnum.REFUND_RETURNED.getCode();
    }

    public void refundPaid() {
        this.refund_status = RefundStatusEnum.REFUND_PAID.getCode();
    }
}
