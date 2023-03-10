package com.acme.acmemall.model;

import com.acme.acmemall.exception.ApiCusException;
import com.acme.acmemall.exception.ResultCodeEnum;
import com.acme.acmemall.factory.OrderFactory;
import com.alibaba.fastjson.JSON;
import com.google.common.collect.Lists;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * @description:
 * @author: ihpangzi
 * @time: 2023/2/18 11:56
 */
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
public class OrderVo implements Serializable {
    //主键
    private String id;
    // 订单编号
    private String order_sn;
    //会员Id
    private Long user_id;
    /*
    订单状态
    1xx 表示订单取消和删除等状态 0订单创建成功等待付款，　101订单已取消，　102订单已删除 . 103 订单已失效不可再次付款
    2xx 表示订单支付状态　201订单已付款，等待发货
    3xx 表示订单物流相关状态　300订单已发货， 301用户确认收货
    4xx 表示订单退换货相关的状态　401 没有发货，退款　402 已收货，退款退货
    */
    private Integer order_status;
    //发货状态 商品配送情况;0未发货,1已发货,2已收货,4退货
    private Integer shipping_status;
    //付款状态 支付状态;0未付款;1付款中;2已付款;4退款
    private Integer pay_status;
    //收货人
    private String consignee;
    //国家
    private String country;
    //省
    private String province;
    //地市
    private String city;
    //区县
    private String district;
    //收货地址
    private String address;
    //联系电话
    private String mobile;
    //补充说明
    private String postscript;
    //快递公司Id
    private Integer shipping_id;
    //快递公司code
    private String shipping_code;
    //快递公司名称
    private String shipping_name;
    //快递号
    private String shipping_no;
    //付款
    private String pay_id;
    //
    private String pay_name;
    //快递费用

    @Builder.Default
    private BigDecimal shipping_fee = BigDecimal.ZERO;
    //实际需要支付的金额
    @Builder.Default
    private BigDecimal actual_price = BigDecimal.ZERO;
    // 积分
    private Integer integral;
    // 积分抵扣金额
    @Builder.Default
    private BigDecimal integral_money = BigDecimal.ZERO;
    //订单总价
    @Builder.Default
    private BigDecimal order_price = BigDecimal.ZERO;

    @Builder.Default
    //商品总价
    private BigDecimal goods_price = BigDecimal.ZERO;
    //新增时间-下单时间
    private Date add_time;

    // 订单删除时间
    private Date delete_time;

    // 取消时间
    private Date cancle_time;
    //确认时间
    private Date confirm_time;
    //付款时间
    private Date pay_time;
    //配送费用
    @Builder.Default
    private BigDecimal freight_price = BigDecimal.ZERO;
    //使用的优惠券id
    private Integer coupon_id;
    //
    private Integer parent_id;
    //优惠价格
    @Builder.Default
    private BigDecimal coupon_price = BigDecimal.ZERO;
    //
    private Integer callback_status;
    //
    private Integer goodsCount; // 订单的商品数量
    private String order_status_text;//订单状态的处理
    private Map handleOption; //可操作的选项

    @Builder.Default
    private BigDecimal full_cut_price = BigDecimal.ZERO; //订单满减
    private String full_region;//区县
    //1购物车、2普通、3秒杀、4团购
    private String order_type; // 订单状态
    private String goods_name;//商品名称
    private String list_pic_url;//图片地址
    private String goods_id;//商品ID

    @Builder.Default
    private BigDecimal all_price = BigDecimal.ZERO;//全部总价
    private String all_order_id;//总订单ID
    //新增
    //推广人id
    private int promoter_id;
    //本订单佣金
    @Builder.Default
    private BigDecimal brokerage = BigDecimal.ZERO;
    //fx状态 默认为0是没有分润金额，已分润状态变成1
    private Integer fx_status;
    //商户id
    private Integer merchant_id;
    //团购ID
    private String group_buying_id;

    // 收货人地址ID
    private long addressId;

    @Builder.Default
    private List<OrderGoodsVo> items = Lists.newArrayList();

    private static void check(OrderVo orderVo, long userId) {
        if (orderVo == null) {
            throw new ApiCusException("订单不存在");
        }
        if (orderVo.getUser_id() != userId) {
            throw new ApiCusException("非法用户不能取消");
        }
    }

    public String getFull_region() {
        //    return full_region;
        if (StringUtils.isNotEmpty(this.full_region)) {
            return full_region;
        } else {
            StringBuffer strBuff = new StringBuffer();
            if (StringUtils.isNotEmpty(this.country)) {
                strBuff.append(this.country.equalsIgnoreCase("CN") ? "中国" : this.country).append(" ");
            }
            if (StringUtils.isNotEmpty(this.province)) {
                strBuff.append(this.province).append(" ");
            }
            if (StringUtils.isNotEmpty(this.city)) {
                strBuff.append(this.city).append(" ");
            }
            if (StringUtils.isNotEmpty(this.district)) {
                strBuff.append(this.district).append(" ");
            }
            this.full_region = strBuff.toString();
            return this.full_region;
        }
    }

    /**
     * 订单提交
     *
     * @param userCouponList 用户优惠优惠
     * @param cartList       购物车明细
     * @param address        收件人地址
     * @return
     */
    public OrderVo submit(List<UserCouponVo> userCouponList, List<ShopCartVo> cartList, AddressVo address) {
        // 订单收件人信息
        setAddressInfo(address);
        // 优惠信息
        if (CollectionUtils.isNotEmpty(userCouponList)) {
            UserCouponVo userCoupon = userCouponList.stream().findFirst().get();
            this.coupon_id = userCoupon.getCoupon_id();
            this.coupon_price = userCoupon.getCoupon_price();
            this.full_cut_price = userCoupon.getCoupon_price();
        }
        // 商品数量
        this.goodsCount = cartList.stream().mapToInt(ShopCartVo::getNumber).sum();
        // 商品总价
        this.goods_price = BigDecimal.valueOf(cartList.stream().mapToDouble(cart -> cart.getGoodsTotalAmount().doubleValue()).sum());
        // 运费
        this.freight_price = BigDecimal.valueOf(cartList.stream().mapToDouble(cart -> cart.getExtraPrice().doubleValue()).sum());
        // 订单实付金额
        this.actual_price = goods_price.add(freight_price).subtract(coupon_price);
        // 订单总价=商品总价+运费
        this.order_price = goods_price.add(freight_price);
        // 总付款金额
        this.all_price = actual_price;
        this.handleOption = OrderHandleOption.builder().build().canOption(this.order_status);

        // 订单明细
        cartList.stream().forEach(cartVo -> this.items.add(OrderFactory.buildOrderItem(cartVo, id)));
        return this;
    }

    /**
     * 付款
     *
     * @return
     */
    public OrderVo prePay(OrderVo orderVo) {
        this.pay_id = UUID.randomUUID().toString();
        this.pay_name = null;
        this.pay_time = new Date();
        // 支付中
        this.pay_status = 1;
        return this;
    }

    /**
     * 付款成功
     * @param orderVo
     * @return
     */
    public OrderVo paid(OrderVo orderVo){
        this.pay_status = 2;
        this.order_status = OrderStatus.TO_BE_SHIPPED.getCode();
        this.order_status_text = OrderStatus.TO_BE_SHIPPED.getDescription();
        this.shipping_status = 0;
        this.pay_time = new Date();
        return this;
    }

    /**
     * 取消订单
     *
     * @return
     */
    public void cancle(OrderVo orderVo, long userId) {
        check(orderVo, userId);
        OrderStatus status = OrderStatus.parse(orderVo.order_status);
        if (status != OrderStatus.TO_BE_PAID) {
            throw new ApiCusException("当前订单状态不支持取消订单");
        }
        this.order_status = OrderStatus.CANCELED.code;
        this.order_status_text = OrderStatus.CANCELED.getDescription();
        this.cancle_time = new Date();
    }

    /**
     * 逻辑删除(标记)
     *
     * @param orderVo
     * @param userId
     */
    public void delete(OrderVo orderVo, long userId) {
        check(orderVo, userId);
        OrderStatus status = OrderStatus.parse(orderVo.order_status);
        if (status != OrderStatus.CANCELED) {
            throw new ApiCusException("当前订单状态不支持删除订单");
        }
        this.order_status = OrderStatus.DELETED.code;
        this.order_status_text = OrderStatus.DELETED.getDescription();
        this.delete_time = new Date();
    }

    /**
     * 评价
     *
     * @return
     */
    public OrderVo grade() {
        return this;
    }

    /**
     * 发货
     *
     * @return
     */
    public OrderVo shipped() {
        return this;
    }

    private void setAddressInfo(AddressVo address) {
        this.addressId = address.getId();
        this.address = address.getDetailInfo();
        this.province = address.getProvinceName();
        this.city = address.getCityName();
        this.district = address.getCountyName();
        this.full_region = address.getFull_region();
        this.country = address.getNationalCode();
        this.consignee = address.getUserName();
        this.mobile = address.getTelNumber();
    }

    /**
     * 订单提交校验操作
     */
    public void checkSubmit() {
        if (this.actual_price.compareTo(BigDecimal.ZERO) <= 0) {
            throw new ApiCusException(ResultCodeEnum.FAILED.getMessage());
        }
        if (CollectionUtils.isEmpty(this.items) || this.items.size() > 1000) {
            throw new ApiCusException(ResultCodeEnum.FAILED.getMessage());
        }
    }

    /**
     * 订单状态描述
     *
     * @return
     */
    public String getOrder_status_text() {
        return OrderStatus.parse(order_status).getDescription();
    }

    public Map getHandleOption() {
        return OrderHandleOption.builder().build().canOption(this.order_status);
    }

    public boolean checkOwner(long userId) {
        if (userId <= 0) {
            return false;
        }
        return userId == this.user_id;
    }

    public boolean canPay() {
        return this.pay_status.compareTo(1) <= 0;
    }

    public String getPayBody_title() {
        if (CollectionUtils.isEmpty(items)) {
            return this.all_order_id;
        }
        List<String> names = items.stream().map(OrderGoodsVo::getGoods_name).collect(Collectors.toList());
        return names.stream().collect(Collectors.joining(",")) + " and so on";

    }

    /**
     * JSON转化
     *
     * @return
     */
    @Override
    public String toString() {
        return JSON.toJSONString(this);
    }
}
