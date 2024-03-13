package com.acme.acmemall.factory;

import com.acme.acmemall.model.OrderGoodsVo;
import com.acme.acmemall.model.OrderVo;
import com.acme.acmemall.model.ShopCartVo;
import com.acme.acmemall.utils.SnowFlakeGenerateIdWorker;

/**
 * @description:
 * @author: ihpangzi
 * @time: 2023/2/22 14:37
 */
public class OrderFactory {

    public static OrderVo buildNewOrder(long userId,String type) {
        String id = SnowFlakeGenerateIdWorker.generateOrderNumber();
        return OrderVo.builder()
                .id(id)
                .order_sn(String.format("wx%s", id))
                .order_status(0)
                .order_type(type)
                .pay_status(0)
                .user_id(userId)
                .shipping_status(0)
                .build();
    }

    public static OrderGoodsVo buildOrderItem(ShopCartVo cartVo,String orderId){
        return OrderGoodsVo.builder()
                .order_id(orderId)
                .goods_id(Long.valueOf(cartVo.getGoods_id()))
                .product_id(Long.valueOf(cartVo.getProduct_id()))
                .goods_specifition_ids(cartVo.getGoods_specifition_ids())
                .goods_specifition_name_value(cartVo.getGoods_specifition_name_value())
                .goods_sn(cartVo.getGoods_sn())
                .goods_name(cartVo.getGoods_name())
                .market_price(cartVo.getMarket_price())
                .retail_price(cartVo.getRetail_price())
                .number(cartVo.getNumber())
                .list_pic_url(cartVo.getList_pic_url())
                .is_real(1)
                .build();
    }
}
