package com.acme.acmemall.model;

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
    private long id;
    private String order_id;
    private long user_id;
    private Integer refund_type;
    private String refund_reason;
    private String goods_info;
    private Integer refund_num;
    private String refund_explain;
    private BigDecimal refund_price;
    private Date add_time;
    private BigDecimal refunded_price;
    private Date refunded_time;
    private String refund_phone;
    private String refund_express;
    private String refund_express_name;
    private String refuse_reason;
    private String remark;
    private Integer refund_status;

    public void submit() {
        this.add_time = new Date();
        this.refund_status = 1;
    }

    public void audit() {
        this.refund_status = 2;
    }
}
