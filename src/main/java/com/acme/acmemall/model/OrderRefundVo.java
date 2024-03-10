package com.acme.acmemall.model;

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

    //    RefundOptionEnum 操作
    private String refundOption;

    public String getRefund_status_text() {
        return RefundStatusEnum.parse(this.refund_status).getName();
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

    /**
     * 商家审批
     */
    public void audit() {
        this.refund_status = RefundStatusEnum.REFUND_PASS.getCode();
        this.refunded_price = refund_price;
        this.refunded_time = new Date();
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

    public boolean canApply() {
        return RefundStatusEnum.canApply(this.refund_status);
    }
}
