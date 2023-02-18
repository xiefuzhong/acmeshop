package com.acme.acmemall.model;

import lombok.Data;
import org.apache.commons.lang.StringUtils;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.Map;

/**
 * @description:
 * @author: ihpangzi
 * @time: 2023/2/18 11:56
 */
@Data
public class OrderVo implements Serializable {
    //主键
    private Integer id;
    //订单序列号
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
    private BigDecimal shipping_fee;
    //实际需要支付的金额
    private BigDecimal actual_price;
    // 积分
    private Integer integral;
    // 积分抵扣金额
    private BigDecimal integral_money;
    //订单总价
    private BigDecimal order_price;
    //商品总价
    private BigDecimal goods_price;
    //新增时间
    private Date add_time;
    //确认时间
    private Date confirm_time;
    //付款时间
    private Date pay_time;
    //配送费用
    private Integer freight_price;
    //使用的优惠券id
    private Integer coupon_id;
    //
    private Integer parent_id;
    //优惠价格
    private BigDecimal coupon_price;
    //
    private Integer callback_status;
    //
    private Integer goodsCount; //订单的商品
    private String order_status_text;//订单状态的处理
    private Map handleOption; //可操作的选项
    private BigDecimal full_cut_price; //订单满减
    private String full_region;//区县
    //1购物车、2普通、3秒杀、4团购
    private String order_type; // 订单状态
    private String goods_name;//商品名称
    private String list_pic_url;//图片地址
    private String goods_id;//商品ID
    private BigDecimal all_price;//全部总价
    private String all_order_id;//总订单ID
    //新增
    //推广人id
    private int promoter_id;
    //本订单佣金
    private BigDecimal brokerage;
    //fx状态 默认为0是没有分润金额，已分润状态变成1
    private Integer fx_status;
    //商户id
    private Integer merchant_id;
    //团购ID
    private String group_buying_id;

    public String getFull_region() {
        //    return full_region;
        if (StringUtils.isNotEmpty(this.full_region)){
            return full_region;
        } else{
            StringBuffer strBuff = new StringBuffer();
            if (StringUtils.isNotEmpty(this.country)){
                strBuff.append(this.country).append(" ");
            }
            if(StringUtils.isNotEmpty(this.province)){
                strBuff.append(this.province).append(" ");
            }
            if (StringUtils.isNotEmpty(this.city)){
                strBuff.append(this.city).append(" ");
            }
            if (StringUtils.isNotEmpty(this.district)){
                strBuff.append(this.district).append(" ");
            }
            this.full_region = strBuff.toString();
            return this.full_region;
        }
    }
}
