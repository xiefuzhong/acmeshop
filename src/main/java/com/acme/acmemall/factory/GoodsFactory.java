package com.acme.acmemall.factory;

import com.acme.acmemall.controller.reqeust.GoodsRequest;
import com.acme.acmemall.model.GoodsVo;

import java.math.BigDecimal;

/**
 * @description:
 * @author: ihpangzi
 * @time: 2024/1/15 14:05
 */

public class GoodsFactory {

    public static GoodsVo createGoods(GoodsRequest request) {
        return GoodsVo.builder()
                .name(request.getName())
                .goods_brief(request.getGoods_brief())
                .goods_unit(request.getGoods_unit())
                .brand_id(request.getBrand_id())
                .category_id(request.getCategory_id())
                .is_delete(request.getStatus())
                .extra_price(BigDecimal.valueOf(request.getExtra_price()))
                .merchantId(request.getMerchantId())
                .primary_pic_url(request.getPrimary_pic_url())
                .list_pic_url(request.getList_pic_url())
                .keywords(request.getKeyword())
                .build();
    }
}
