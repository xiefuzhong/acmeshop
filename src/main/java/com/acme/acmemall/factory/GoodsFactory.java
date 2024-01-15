package com.acme.acmemall.factory;

import com.acme.acmemall.model.GoodsVo;

/**
 * @description:
 * @author: ihpangzi
 * @time: 2024/1/15 14:05
 */
public class GoodsFactory {
    public static GoodsVo createGoods() {
        return GoodsVo.builder().build();
    }
}
