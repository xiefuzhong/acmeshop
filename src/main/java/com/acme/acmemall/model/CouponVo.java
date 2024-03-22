package com.acme.acmemall.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

@Data
public class CouponVo implements Serializable {
    private static final long serialVersionUID = 1L;

    // 优惠券主键
    private Integer id;
    // 优惠券类型
    private Integer type; // 0:满减 1:折扣
    // 适用类型 通用券-0，商品券-1，品类券-2  商品券-需要指定适用商品，品类券-需要指定适用品类
    private Integer scope;
    // 用户优惠券主键
    private Integer user_coupon_id;
    //优惠券名称
    private String name;
    //金额/面值  优惠券类型-0-满减券-金额数值，优惠券类型-1-折扣券-百分比
    private BigDecimal type_money;
    //发放方式 0：按订单发放 1：按用户发放 2:商品转发送券 3：按商品发放 4:新用户注册 5：线下发放 6评价好评红包（固定或随机红包） 7包邮 8用户主动领取优惠券
    // 领取方式 手动领取(8用户主动领取优惠券)、后台发放  手动领取：用户需要在移动端的领券中心领取优惠券；后台发放：后台发放用于后台发放指定用户使用，移动端不能领取；
    private Integer send_type;

    //    使用门槛 最低消费金额为0,即无门槛。大于0.0元，即达到这个金额可用。
    //最小金额
    private BigDecimal min_amount;
    //最大金额
    private BigDecimal max_amount;
    //发放时间
    @JsonFormat(pattern = "yyyy.MM.dd")
    private Date send_start_date;
    //发放时间
    @JsonFormat(pattern = "yyyy.MM.dd")
    private Date send_end_date;
    //使用开始时间
    @JsonFormat(pattern = "yyyy.MM.dd")
    private Date use_start_date;
    //使用结束时间
    @JsonFormat(pattern = "yyyy年MM月dd日")
    private Date use_end_date;
    //最小商品金额
    private BigDecimal min_goods_amount;
    //优惠券说明(使用规则说明)
    private String coupon_txt;
    //优惠券会员Id
    private String user_id;
    //优惠券编码
    private String coupon_number;
    //可用 1:可用 0：不可用
    private Integer enabled = 0;
    //转发次数
    private Integer min_transmit_num;
    //优惠券状态 1 可用 2 已用 3 过期
    private Integer coupon_status = 1;
    //商户id
    private Long merchantId;

    // 0不限量，大于0:数量限制
    private Integer totalCount;
}
