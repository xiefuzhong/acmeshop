package com.acme.acmemall.model;

import com.acme.acmemall.common.TimeConstants;
import com.acme.acmemall.controller.reqeust.LogisticsInfo;
import com.acme.acmemall.controller.reqeust.OrderRefundRequest;
import com.acme.acmemall.controller.reqeust.OrderShippedRequest;
import com.acme.acmemall.exception.ApiCusException;
import com.acme.acmemall.exception.Assert;
import com.acme.acmemall.exception.ResultCodeEnum;
import com.acme.acmemall.factory.OrderFactory;
import com.acme.acmemall.factory.OrderRefundFactory;
import com.acme.acmemall.model.enums.*;
import com.acme.acmemall.utils.DateUtils;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.fasterxml.jackson.annotation.JsonFormat;
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
    // 半个小时
//    private static final long EXPIRE_TIME = 1800;
    //主键
    private String id;
    // 订单编号
    private String order_sn;
    //会员Id
    private Long user_id;

    /*
    订单状态
    1xx 表示订单取消和删除等状态 0订单创建成功等待付款，　101订单已取消，　102订单已删除 . 103 订单已失效不可再次付款
    2xx 表示订单支付状态　201订单已付款，等待发货(已打单，待发货)，200已付款
    3xx 表示订单物流相关状态　300订单已发货， 301用户确认收货
    4xx 表示订单退换货相关的状态　401 没有发货，退款　402 已收货，退款退货
    */

    // 未成交订单101,401,402,103
    private Integer order_status;
    //发货状态 商品配送情况;0未发货,1已发货,2已收货,4退货
    private Integer shipping_status;
    private String shipping_status_text;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date shipping_time; // 发货时间
    //付款状态 支付状态;0未付款;1付款中;2已付款;4退款
    private Integer pay_status;

    // 售后过期时间
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date refund_expire_time;

    public String getPay_status_text() {
        return PayStatusEnum.parse(this.pay_status).getName();
    }

    private String pay_status_text;

    // 退款状态 0未申请，1申请  2-审核通过 3-买家退还商品 4-已退款 5-拒绝 6-取消申请 7完结 8失效 9-卖家入库
    // 商品待退货
    @Builder.Default
    private Integer refund_status = 0;

    private String refund_pay_id;

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
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date add_time;
    private String fmt_add_time;

    // 过期时间
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date expire_time;

    // 订单删除时间
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date delete_time;

    // 取消时间
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date cancle_time;

    // 取消原因
    private String cancel_reason;

    //确认收货时间
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date confirm_time;
    //付款时间
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
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
    private Long merchant_id;
    //团购ID
    private String group_buying_id;

    // 收货人地址ID
    private long addressId;

    // 发票抬头ID
    private long invoiceHeaderId;

    // 商家备注
    private String merRemark;

    //    客户备注
    private String cusRemark;

    private String handleType;

    // 退货/售后地址
    private AddressVo returnAddress;
    // 发货（可默认为退货/售后地址)
    private AddressVo shippingAddress;

    @Builder.Default
    private List<OrderGoodsVo> items = Lists.newArrayList();

    private String goodsIds;

    // 评论状态 0-未评论
    @Builder.Default
    private Integer comment_status = 0;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date comment_time; // 评论时间

    private OrderRefundVo refundVo;

    // 1-仅退款 2-退货退款 RefundType.class
    private Integer refund_type;
    private String refund_type_text;

    //  自动收货时间
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date rog_time;

    // 售后过期时间
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date after_service_expire_time;

    public String getRefund_type_text() {
        return RefundType.parse(this.refund_type).getTitle();
    }

    // 订单操作记录
    private String orderProcessText = StringUtils.EMPTY;

    @Builder.Default
    private List<OrderProcessVo> orderProcessList = Lists.newArrayList();

    public List<OrderProcessVo> getOrderProcessList() {
        if (StringUtils.isNotEmpty(this.orderProcessText)) {
            JSONArray processArr = JSON.parseArray(orderProcessText);
            this.orderProcessList = JSONArray.parseArray(processArr.toJSONString(), OrderProcessVo.class);
        }
        return orderProcessList;
    }


    // 评论
    @Builder.Default
    private List<CommentVo> commentList = Lists.newArrayList();

    private static void check(OrderVo orderVo, long userId) {
        if (orderVo == null) {
            throw new ApiCusException("订单不存在");
        }
        if (!orderVo.getUser_id().equals(userId)) {
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
                strBuff.append(StringUtils.equalsIgnoreCase("CN", this.country) ? "中国" : this.country).append(" ");
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
     * @param invoiceTitleVo 发票信息
     * @return
     */
    public OrderVo submit(List<UserCouponVo> userCouponList, List<ShopCartVo> cartList, AddressVo address, InvoiceTitleVo invoiceTitleVo) {
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
        this.handleOption = OrderOperationOption.builder().build().buyerOption(this.order_status);
        this.merchant_id = cartList.stream().findFirst().get().getMerchant_id();
        this.add_time = new Date();
        // 支付过期时间(状态扫描任务执行时间)
        this.expire_time = new Date(add_time.getTime() + TimeConstants.PAY_EXPIRE_TIME);
        // 订单明细
        cartList.stream().forEach(cartVo -> this.items.add(OrderFactory.buildOrderItem(cartVo, id)));
        this.addProcess("您的提交了订单," + OrderStatusEnum.NEW.getName());
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
        this.pay_status = PayStatusEnum.PAY_ING.getCode();
        return this;
    }

    /**
     * 付款成功
     *
     * @return
     */
    public OrderVo paid() {
        this.pay_status = PayStatusEnum.PAY_YES.getCode();
        this.order_status = OrderStatusEnum.PAID.getCode();
        this.order_status_text = OrderStatusEnum.PAID.getName();
        this.shipping_status = ShipStatusEnum.SHIP_NO.getCode();
        this.shipping_status_text = ShipStatusEnum.SHIP_NO.getName();
        this.pay_time = new Date();
        this.addProcess(String.format("您的订单已支付成功", OrderStatusEnum.PAID.getName()));
        return this;
    }

    private void addProcess(String desc) {
        int sort_id = 1;
        List<OrderProcessVo> processList = Lists.newArrayList();
        if (StringUtils.isNotEmpty(this.orderProcessText)) {
            JSONArray processArr = JSON.parseArray(orderProcessText);
            processList = JSONArray.parseArray(processArr.toJSONString(), OrderProcessVo.class);
            sort_id = processList.size() + 1;
        }

        OrderProcessVo process = new OrderProcessVo(DateUtils.timeToStr(new Date().getTime(), DateUtils.DATE_TIME_PATTERN), desc, sort_id);
        processList.add(0, process);
        this.orderProcessText = JSON.toJSONString(processList);
        this.orderProcessList = processList;
    }

    /**
     * 取消订单
     *
     * @return
     */
    public void cancle(OrderVo orderVo, long userId) {
        check(orderVo, userId);
        // 待付款、已付款、已付款未发货可取消
        if (!orderVo.canCancel()) {
            throw new ApiCusException("当前订单状态不支持取消订单");
        }
        this.cancel_reason = "用户手动取消订单";
        this.cancle(this.cancel_reason);
    }

    public void cancle(String cancel_reason) {
        this.order_status = OrderStatusEnum.CANCELED.getCode();
        this.order_status_text = OrderStatusEnum.CANCELED.getName();
        this.cancle_time = new Date();
        this.cancel_reason = cancel_reason;
        this.addProcess(String.format("订单%s:%s", OrderStatusEnum.CANCELED.getName(), cancel_reason));
    }

    public boolean canCancel() {
        OrderStatusEnum statusEnum = OrderStatusEnum.parse(this.order_status);
        return statusEnum == OrderStatusEnum.NEW;
    }

    /**
     * 逻辑删除(标记)
     *
     * @param orderVo
     * @param userId
     */
    public void delete(OrderVo orderVo, long userId) {
        check(orderVo, userId);
        OrderStatusEnum status = OrderStatusEnum.parse(orderVo.order_status);
        // 完结的订单可删除
        if (status != OrderStatusEnum.CLOSED && status != OrderStatusEnum.CANCELED) {
            throw new ApiCusException("当前订单状态不支持删除订单");
        }
        this.order_status = OrderStatusEnum.DELETED.getCode();
        this.order_status_text = OrderStatusEnum.DELETED.getName();
        this.delete_time = new Date();
        this.addProcess(String.format("订单%s", OrderStatusEnum.DELETED.getName()));
    }

    /**
     * 确认收货
     */
    public void confirm() {
        this.order_status = OrderStatusEnum.ROG.getCode();
        this.order_status_text = OrderStatusEnum.ROG.getName();
        this.confirm_time = new Date();
        this.shipping_status = ShipStatusEnum.SHIP_ROG.getCode();
        this.shipping_status_text = ShipStatusEnum.SHIP_ROG.getName();
        this.addProcess(String.format("您的订单%s", OrderStatusEnum.ROG.getName()));
        this.refund_expire_time = new Date(this.confirm_time.getTime() + TimeConstants.AFTER_SALES_EXPIRE_TIME);
    }

    /**
     * 评价
     *
     * @return
     */
    public void comment() {
        this.comment_status = 1;
        this.comment_time = new Date();
        CommentVo comment = CommentVo.builder()
                .user_id(this.user_id)
                .order_id(this.id)
                .goods_name(this.goods_name)
                .score(5)
                .content("此用户未填写评价内容")
                .build();
        this.commentList.add(comment);
    }


    /**
     * 发货
     *
     * @return
     */
    public OrderVo shipped(OrderShippedRequest shippedRequest) {
        this.setLogistics(shippedRequest.getLogisticsInfo());
        // 客户待收货
        this.order_status = OrderStatusEnum.SHIPPED.getCode();
        this.order_status_text = OrderStatusEnum.SHIPPED.getName();
        // 物流已发货
        this.shipping_status = ShipStatusEnum.SHIP_YES.getCode();
        this.shipping_status_text = ShipStatusEnum.SHIP_YES.getName();
        // 发货时间
        this.shipping_time = new Date();
        this.shipping_fee = BigDecimal.ZERO;
        // 收货确认过期时间
        this.rog_time = new Date(this.shipping_time.getTime() + TimeConstants.PAY_EXPIRE_TIME);
        // 售后过期时间
        this.after_service_expire_time = new Date(this.shipping_time.getTime() + TimeConstants.AFTER_SALES_EXPIRE_TIME);
        this.addProcess(String.format("您的订单%s", OrderStatusEnum.SHIPPED.getName()));
        return this;
    }

    private void setLogistics(LogisticsInfo logisticsInfo) {
        this.shipping_code = logisticsInfo.getDelivery_id();
        this.shipping_no = logisticsInfo.getShipping_no();
        this.shipping_name = logisticsInfo.getDelivery_name();
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

    private void setInvoiceTitleVo(InvoiceTitleVo invoiceTitleVo) {
        this.invoiceHeaderId = invoiceTitleVo.getId();
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
        return OrderStatusEnum.parse(order_status).getName();
    }

    /**
     * 物流状态
     *
     * @return
     */
    public String getShipping_status_text() {
        return ShipStatusEnum.parse(this.shipping_status).getName();
    }

    /**
     * 售后状态
     *
     * @return
     */
    public String getRefund_status_text() {
        return RefundStatusEnum.parse(this.refund_status).getName();
    }

    public void buildHandleOption(long merchantId) {
        this.handleOption = merchantId > 0 ? this.getMerchantOption() : this.getBuyerOption();
    }

    private Map getBuyerOption() {
        Map<String, Boolean> optionMap = OrderOperationOption.builder().build().buyerOption(this.order_status);
        if (OrderStatusEnum.endCheck(this.order_status)) {
            return optionMap;
        }
        OrderStatusEnum orderStatus = OrderStatusEnum.parse(this.order_status);
        // 售后操作
        if (orderStatus == OrderStatusEnum.AFTER_SERVICE) {
            RefundStatusEnum refundStatus = RefundStatusEnum.parse(this.refund_status);
            switch (refundStatus) {
                case REFUND_REJECT:
                case REFUND_CANCEL:
                case REFUND_NO: {
                    // 可申请
                    optionMap.put("refundRequest", Boolean.TRUE);
                    optionMap.put("fillInLogistics", Boolean.FALSE);
                    break;
                }
                case REFUND_APPLY: {
                    // 可取消
                    optionMap.put("cancelRefundRequest", Boolean.TRUE);
                    break;
                }
                case REFUND_PASS: {
                    // 审批通过填写退货物流信息
                    optionMap.put("fillInLogistics", Boolean.TRUE);
                    break;
                }
                default: {
                    optionMap.put("fillInLogistics", Boolean.FALSE);
                    optionMap.put("cancelRefundRequest", Boolean.FALSE);
                    break;
                }
            }
        }

        if (this.comment_status == 1) {
            optionMap.put("comment", Boolean.FALSE);
        }
        return optionMap;
    }

    private Map getMerchantOption() {
        Map<String, Boolean> optionMap = OrderOperationOption.builder().build().merchantOperation(this.order_status);
        // 退款状态 0未申请，1申请  2-审核通过 3-已退款 4-拒绝 5-取消申请 6完结 7失效
        OrderStatusEnum orderStatus = OrderStatusEnum.parse(this.order_status);
        if (orderStatus == OrderStatusEnum.AFTER_SERVICE) {
            RefundStatusEnum refundStatus = RefundStatusEnum.parse(this.refund_status);
            switch (refundStatus) {
                case REFUND_APPLY: {
                    // 卖家审核
                    optionMap.put("refundAudit", Boolean.TRUE);
                    break;
                }
                case REFUND_RECEIVED: {
                    // 商品待退还入库
                    optionMap.put("confirmReceipt", Boolean.TRUE);
                    break;
                }
                case REFUND_PASS:
                case REFUND_RETURNED: {
                    if ((refundStatus == RefundStatusEnum.REFUND_PASS && RefundType.isRefundOnly(this.refund_type))
                            || (refundStatus == RefundStatusEnum.REFUND_RETURNED && RefundType.isRefundReturn(this.refund_type))) {
                        optionMap.put("refundMoney", true);
                    }
                    break;
                }
                default: {
                    optionMap.put("refundAudit", Boolean.FALSE);
                    optionMap.put("confirmReceipt", Boolean.FALSE);
                    optionMap.put("refundMoney", Boolean.FALSE);
                    break;
                }
            }
        }
        return optionMap;
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
            return this.id;
        }
        List<String> names = items.stream().map(OrderGoodsVo::getPayBody_title).collect(Collectors.toList());
        return names.stream().collect(Collectors.joining(",")) + " and so on";

    }

    public String getFmt_add_time() {
        return DateUtils.timeToUtcDate(this.add_time.getTime(), DateUtils.DATE_TIME_PATTERN);
    }


    public Boolean paidCheck() {
        return pay_status.intValue() == PayStatusEnum.PAY_YES.getCode();
    }

    public void resetPayStatus() {
        this.pay_status = PayStatusEnum.PAY_NO.getCode();
    }

    public Boolean refundStatus() {
        return this.order_status == 401 || this.order_status == 402;
    }

    /**
     * 退款操作，未发货-退款
     */
    public void refund() {
        if (this.order_status == OrderStatusEnum.AFTER_SERVICE.getCode()) {
            this.order_status = OrderStatusEnum.CLOSED.getCode();
            this.order_status_text = OrderStatusEnum.CLOSED.getName();
            this.pay_status = PayStatusEnum.PAY_REFUND.getCode();
            this.refund_status = 3;
        }
    }

    public void closed(OrderRefundVo refundVo) {
        Assert.isNull(refundVo, "无售后信息，不能结束交易");
        Assert.isNull(refundVo.getRefund_type(), "无售后类型，不能结束交易");
        OrderStatusEnum orderStatus = OrderStatusEnum.parse(this.order_status);
        // 订单售后中
        if (orderStatus == OrderStatusEnum.AFTER_SERVICE) {
            this.order_status = OrderStatusEnum.CLOSED.getCode();
            this.pay_status = PayStatusEnum.PAY_REFUND.getCode();
            this.refundVo = refundVo;
            this.refund_status = RefundStatusEnum.REFUND_PAID.getCode();
        }
    }

    /**
     * 未发货退款
     */
    public void unshippedRefund(OrderRefundVo refundVo) {
        OrderStatusEnum orderStatus = OrderStatusEnum.parse(this.order_status);
        if (orderStatus == OrderStatusEnum.AFTER_SERVICE) {
            this.order_status = OrderStatusEnum.CLOSED.getCode();
            this.order_status_text = OrderStatusEnum.CLOSED.getName();
            this.pay_status = PayStatusEnum.PAY_REFUND.getCode();
            this.refundVo = refundVo;
            this.refund_status = RefundStatusEnum.REFUND_PAID.getCode();
        }
    }

    /**
     * 售后服务
     *
     * @param refundVo
     */
    public void afterService(OrderRefundVo refundVo, String refundOption) {
        OrderStatusEnum orderStatus = OrderStatusEnum.parse(this.order_status);
        if (orderStatus == OrderStatusEnum.AFTER_SERVICE) {
            this.refundVo = refundVo;
            RefundOptionEnum option = RefundOptionEnum.parse(refundOption);
            switch (option) {
                case CANCEL: {
                    // 取消申请
                    this.refundVo.cancel();
                    this.refundUpdate();
                    break;
                }
                case REJECT: {
                    this.refundVo.reject();
                    this.refundUpdate();
                    break;
                }
                case AUDIT: {
                    this.refundVo.audit();
                    break;
                }
                case RETURN:
                case LOGISTICS: {
                    this.refundVo.refundReturned();
                    // 货物退回
                    this.shipping_status = ShipStatusEnum.SHIP_RETURN.getCode();
                    break;
                }
                case REFUND: {
                    this.refundVo.refundPaid();
                    if (refundVo.getRefund_type() == 1) {
                        this.order_status = OrderStatusEnum.REFUNDED.getCode();
                    } else if (refundVo.getRefund_type() == 2) {
                        this.order_status = OrderStatusEnum.REFUND_RETURNED.getCode();
                    }
                    break;
                }
                case SUBMIT: {
                    this.refundVo.submit(this.user_id);
                    this.refund_type = refundVo.getRefund_type();
                    break;
                }
                case RECEIPT: {
                    this.refundVo.confirm();
                    break;
                }
                default:
            }
            this.refund_status = this.refundVo.getRefund_status();
        }
    }

    private void refundUpdate() {
        if (this.shipping_status == ShipStatusEnum.SHIP_YES.getCode()) {
            // 已发货,订单状态重置为已发货
            this.order_status = OrderStatusEnum.SHIPPED.getCode();
        } else if (this.shipping_status == ShipStatusEnum.SHIP_NO.getCode()) {
            // 未发货，订单状态重置为 待发货
            this.order_status = OrderStatusEnum.PAID.getCode();
        } else if (this.shipping_status == ShipStatusEnum.SHIP_ROG.getCode()) {
            // 订单重置为已收货
            this.order_status = OrderStatusEnum.ROG.getCode();
        }
    }

    public void refundRequest(OrderRefundRequest request) {
        this.order_status = OrderStatusEnum.AFTER_SERVICE.getCode();
        this.order_status_text = OrderStatusEnum.AFTER_SERVICE.getName();
        this.refund_status = RefundStatusEnum.REFUND_APPLY.getCode();
        this.refundVo = OrderRefundFactory.build(request, this.user_id);
        refundVo.submit(this.user_id);
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

    public void handle(OrderVo orderVo) {
        if ("remark".equals(orderVo.getHandleType())) {
            this.merRemark = orderVo.getMerRemark();
        }
    }

    public void fillItem(List<OrderGoodsVo> items) {
        if (CollectionUtils.isNotEmpty(items)) {
            this.items.addAll(items);
        }
    }


    public void merReceipt(OrderRefundVo refundVo) {
        // 商家确认收货
        this.refund_status = RefundStatusEnum.REFUND_RETURNED.getCode();
        // 物流：货物已退回
        this.shipping_status = ShipStatusEnum.SHIP_RETURN.getCode();
        refundVo.confirm();
        this.refundVo = refundVo;
    }

    public Boolean canRefund(OrderRefundVo refundVo) {
        if (refundVo == null) {
            return Boolean.FALSE;
        }
        if (this.order_status != OrderStatusEnum.AFTER_SERVICE.getCode()) {
            return Boolean.FALSE;
        }
        if (refundVo.getRefund_type() == 1) {
            // 仅退款(审核通过&&已付款)
            return this.refund_status == RefundStatusEnum.REFUND_PASS.getCode()
                    && this.pay_status == PayStatusEnum.PAY_YES.getCode();
        } else if (refundVo.getRefund_type() == 2) {
            // 货物退回 && 商家签收
            if (this.shipping_status == ShipStatusEnum.SHIP_RETURN.getCode()
                    && this.refund_status == RefundStatusEnum.REFUND_RETURNED.getCode()
                    && this.pay_status == PayStatusEnum.PAY_YES.getCode()) {
                return Boolean.TRUE;
            }
        }
        return Boolean.FALSE;
    }

    public void autoEnd() {
        OrderStatusEnum orderStatus = OrderStatusEnum.parse(this.order_status);
        switch (orderStatus) {
            case CANCELED: {
                this.order_status = OrderStatusEnum.CLOSED.getCode();
                this.addProcess(String.format("%s:系统自动关闭", OrderStatusEnum.CLOSED.getName()));
                break;
            }
            case REFUNDED:
            case REFUND_RETURNED: {
                this.order_status = OrderStatusEnum.COMPLETE.getCode();
                this.addProcess(String.format("%s,感谢您的惠顾,阿可美欢迎您再次光临", OrderStatusEnum.COMPLETE.getName()));
                break;
            }
            default:
//            case ROG: {
//                this.order_status = OrderStatusEnum.COMPLETE.getCode();
//                this.addProcess(String.format("%s,感谢您的惠顾,阿可美欢迎您再次光临", OrderStatusEnum.COMPLETE.getName()));
//                this.comment();
//                break;
//            }
        }
    }
}
