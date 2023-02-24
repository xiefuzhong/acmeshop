package com.acme.acmemall.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
public class OrderGoodsVo implements Serializable {
    private static final long serialVersionUID = 1L;

    //主键
    private Integer id;
    //订单Id
    private Integer order_id;
    //商品id
    private Integer goods_id;
    //商品名称
    private String goods_name;
    //商品序列号
    private String goods_sn;
    //产品Id
    private Integer product_id;
    //商品数量
    private Integer number;
    //市场价
    private BigDecimal market_price;
    //零售价格
    private BigDecimal retail_price;
    //商品规格详情
    private String goods_specifition_name_value;
    //虚拟商品
    private Integer is_real;
    //商品规格Ids
    private String goods_specifition_ids;
    //图片链接
    private String list_pic_url;

    //使用的优惠券id
    private Integer coupon_id;
}
