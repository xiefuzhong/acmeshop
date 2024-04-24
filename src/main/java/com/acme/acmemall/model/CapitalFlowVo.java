package com.acme.acmemall.model;

import com.acme.acmemall.model.enums.PayType;
import com.acme.acmemall.model.enums.TradeType;
import lombok.Data;

import java.math.BigDecimal;

/**
 * @description: 资金流水o
 * @author: ihpangzi
 * @time: 2024/4/21 13:44
 */
@Data
public class CapitalFlowVo implements java.io.Serializable {
    private Long id;

    private String flow_id; // 交易单号

    private String order_id; // 关联订单号

    private Integer trade_type; // 交易类型 支付订单、退款订单、提现订单

    private String trade_type_title;

    private Long add_time; // 交易时间

    private Long user_id; // 用户id

    private String nickname; // 昵称

    private String mobile; // 手机号

    private BigDecimal trade_amount; //   交易金额

    private Integer pay_type; // 支付方式  微信支付、支付宝、银行卡、余额支付

    private String remark; // 备注

    private String pay_type_title; // 支付方式名称

    public String getPay_type_title() {
        PayType type = PayType.parse(this.pay_type);
        return type == null ? "" : type.getTitle();
    }

    public String getTrade_type_title() {
        TradeType type = TradeType.parse(this.trade_type);
        return type == null ? "" : type.getTitle();
    }
}
