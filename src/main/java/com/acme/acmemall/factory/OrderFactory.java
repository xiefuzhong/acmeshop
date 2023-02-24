package com.acme.acmemall.factory;

import com.acme.acmemall.model.OrderGoodsVo;
import com.acme.acmemall.model.OrderVo;
import com.acme.acmemall.model.ShopCartVo;
import com.acme.acmemall.utils.SnowFlakeGenerateIdWorker;

import java.util.Date;
import java.util.UUID;

/**
 * @description:
 * @author: ihpangzi
 * @time: 2023/2/22 14:37
 */
public class OrderFactory {

    public static OrderVo buildCartOrder(long userId) {
        String id = new SnowFlakeGenerateIdWorker(0,0).generateNextId();
        return OrderVo.builder()
                .id(id)
                .all_order_id(UUID.randomUUID().toString().replaceAll("-", ""))
                .add_time(new Date())
                .order_status(0)
                .pay_status(0)
                .user_id(userId)
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
