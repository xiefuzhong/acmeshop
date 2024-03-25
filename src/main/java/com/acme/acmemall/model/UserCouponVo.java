package com.acme.acmemall.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * @description:
 * @author: ihpangzi
 * @time: 2023/2/14 10:29
 */
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserCouponVo implements Serializable {
    //主键
    private Integer id;
    //优惠券Id
    private Integer coupon_id;
    //优惠券数量
    private String coupon_number;
    //会员Id
    private Long user_id;
    //使用时间
    private Date used_time;
    //领取时间/发放时间
    private Date add_time;
    //订单Id
    private Integer order_id;
    //来源key
    private String source_key;
    //分享人
    private Long referrer;
    //优惠券金额
    private BigDecimal coupon_price;
    private Long merchantId;
    private Integer totalCount;
    //状态 1. 可用 2. 已用 3. 过期
    private Integer coupon_status;

    //最小使用金额
    private BigDecimal min_goods_amount;

    //使用开始时间
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date use_start_date;

    //使用结束时间
    @JsonFormat(pattern = "yyyy年MM月dd日")
    private Date use_end_date;

    public void exchange(long user_id){
        this.add_time = new Date();
        this.user_id = user_id;
    }
}
