package com.acme.acmemall.factory;

import com.acme.acmemall.controller.reqeust.GoodsRequest;
import com.acme.acmemall.model.GoodsVo;
import com.acme.acmemall.model.LoginUserVo;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @description:
 * @author: ihpangzi
 * @time: 2024/1/15 14:05
 */

public class GoodsFactory {

    public static GoodsVo createGoods(GoodsRequest request, LoginUserVo userVo) {
        return GoodsVo.builder()
                .operator(userVo)
                .name(request.getName())
                .goods_brief(request.getGoods_brief())
                .goods_unit(request.getGoods_unit())
                .brand_id(request.getBrand_id())
                .category_id(request.getCategory_id())
                .is_on_sale(request.getStatus())
                .extra_price(BigDecimal.valueOf(request.getExtra_price()))
                .merchantId(request.getMerchantId())
                .primary_pic_url(request.getPrimary_pic_url())
                .list_pic_url(request.getList_pic_url())
                .keywords(request.getKeyword())
                .add_time(new Date())
                .is_delete(0)
                .goods_number(0l)
                .goods_desc(request.getGoodsDesc())
                .build();
    }
}
