package com.acme.acmemall.model;

import com.acme.acmemall.controller.reqeust.CouponRequest;
import com.acme.acmemall.model.enums.CouponStatusEnum;
import com.acme.acmemall.model.enums.CouponTypeEnum;
import com.acme.acmemall.model.enums.ScopeEnum;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Date;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
public class CouponVo implements Serializable {
    private static final long serialVersionUID = 1L;

    // 优惠券主键
    private Long id;

    // 优惠券添加时间
    private Date add_time;

    // 优惠券类型 1:满减 2:折扣
    private Integer type;

    private String type_title;

    // 适用类型 通用券-0，商品券-1，品类券-2  商品券-需要指定适用商品，品类券-需要指定适用品类
    private Integer scope; //适用类型 0-通用 1-品类券 2-商品券

    private String scope_title;

    // 用户优惠券主键
    private Integer user_coupon_id;

    //优惠券名称
    private String name;

    public BigDecimal getType_money() {
        return type_money == null ? null : type_money.setScale(2, RoundingMode.HALF_UP);
    }

    //金额/面值  优惠券类型-0-满减券-金额数值，优惠券类型-1-折扣券-百分比
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private BigDecimal type_money;

    // 领取方式 手动领取(8用户主动领取优惠券)、后台发放  手动领取：用户需要在移动端的领券中心领取优惠券；后台发放：后台发放用于后台发放指定用户使用，移动端不能领取；
    private Integer send_type;

    private String send_type_title;

    public BigDecimal getMin_amount() {
        return min_amount == null ? null : min_amount.setScale(2, RoundingMode.HALF_UP);
    }

    //最小金额 使用门槛 最低消费金额为0,即无门槛。大于0.0元，即达到这个金额可用。
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private BigDecimal min_amount;

    public BigDecimal getMax_amount() {
        return max_amount == null ? null : max_amount.setScale(2, RoundingMode.HALF_UP);
    }

    //最大金额
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private BigDecimal max_amount;

    //发放时间
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date send_start_date;

    //发放时间
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date send_end_date;

    //使用开始时间
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date use_start_date;

    //使用结束时间
    @JsonFormat(pattern = "yyyy年MM月dd日")
    private Date use_end_date;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date expired_time;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date received_time;

    public BigDecimal getMin_goods_amount() {
        return min_goods_amount == null ? null : min_goods_amount.setScale(2, RoundingMode.HALF_UP);
    }

    //最小商品金额
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private BigDecimal min_goods_amount;

    //优惠券说明(使用规则说明)
    private String coupon_txt;

    //创建人
    private long user_id;

    //优惠券编码-唯一，
    private String coupon_number;

    //优惠券状态 1 可用 2 已用 3 过期 4-已用完。
    private Integer enabled = 0;

    //转发次数
    private Integer min_transmit_num;

    //优惠券状态 1 可用 2 已用 3 过期
    private Integer coupon_status = 1;

    private String coupon_status_text;

    public void updateUsed_status(Integer used_status) {
        this.used_status = used_status;
    }

    // 0-未领取，1-已领取
    private Integer used_status;

    //商户id
    private Long merchantId;

    // 发行量：>1000
    private Integer totalCount;

    // 剩余量
    private Integer remainCount;

    // 已使用
    private Integer useCount;

    // 限制使用次数
    private Integer limit;

    // 使用时间(天-根据领取和发放计算领取和使用有效期)
    private Integer usageTime;

    public void setCoupon_status(Integer coupon_status) {
        this.enabled = coupon_status;
    }

    public String getType_title() {
        return CouponTypeEnum.parse(type).getTitle();
    }

    public String getScope_title() {
        return ScopeEnum.parse(scope).getTitle();
    }

    public String getSend_type_title() {
        return send_type_title;
    }

    public String getCoupon_status_text() {
        return coupon_status_text;
    }

    /**
     * 创建优惠券
     *
     * @param request 请求参数
     */
    public void create(CouponRequest request) {
        this.name = request.getName();
        this.type = request.getType();
        this.send_type = request.getSend_type();
        this.type_money = request.getType_money();
        this.scope = request.getScope();
        this.enabled = request.getEnabled();
        this.totalCount = request.getTotal();
        // 初始剩余量=发行量
        this.remainCount = totalCount;
        this.limit = request.getLimit();
        // 使用门槛
        this.min_amount = request.getMin_amount();
        this.min_goods_amount = request.getMin_amount();
        this.usageTime = request.getUsageTime();
        this.merchantId = request.getMerchantId();
    }

    /**
     * 发放优惠券
     */
    public void send() {

    }

    /**
     * 用户领取优惠券
     */
    public void receive() {
        if (this.remainCount > 0) {
            this.remainCount -= 1;
        }
        if (this.remainCount <= 0) {
            this.coupon_status = CouponStatusEnum.COUPON_NO_AVAILABLE.getCode();
        }
    }

    public void update() {

    }
}
