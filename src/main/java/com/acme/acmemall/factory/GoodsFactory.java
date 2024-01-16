package com.acme.acmemall.factory;

import com.acme.acmemall.controller.reqeust.GoodsRequest;
import com.acme.acmemall.model.GoodsVo;

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
                .is_on_sale(request.getStatus())
                .build();
    }
}
