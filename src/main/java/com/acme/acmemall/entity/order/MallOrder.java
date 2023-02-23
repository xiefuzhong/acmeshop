package com.acme.acmemall.entity.order;

import com.acme.acmemall.model.AddressVo;
import com.acme.acmemall.model.ProductVo;
import com.acme.acmemall.model.ShopCartVo;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * @description:订单
 * @author: ihpangzi
 * @time: 2023/2/22 15:11
 */
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MallOrder {
    /**
     * 主键ID
     */
    long id;

    /**
     * 订单号,雪花ID
     */
    String orderNo;

    /**
     * 订单类型：1购物车、2普通、3秒杀、4团购
     */
    String orderType;

    /**
     * 收件人地址
     */
    AddressVo receiverAddress;

    /**
     * 商品
     */
    List<ProductVo> products;

    /**
     * 订单总额
     */
    BigDecimal orderTotal;

    /**
     * 优惠金额
     */
    BigDecimal couponTotal;

    /**
     * 运费
     */
    BigDecimal freightTotal;

    /**
     * 订单创建时间
     */
    Date createTime;

    /**
     * 订单最后更新时间
     */
    Date lastUpdateTime;

    /**
     * goods_id,product_id
     */
    List<ShopCartVo> carts;

    /**
     * 订单状态,初始状态，待付款(默认)，待发货，待收货,待评价，已完成，已取消
     */
    @Builder.Default
    OrderStatus orderStatus = OrderStatus.NON_PAYMENT;

    /**
     * 物流信息
     */
    Logistics logistics;

    /**
     * 修改收货地址
     * @param addressId
     */
    public void updateReceiverAddress(long addressId){

    }

    /**
     * 付款
     */
    public void pay(){

    }

    /**
     * 取消订单
     */
    public void cancle(){

    }
}
