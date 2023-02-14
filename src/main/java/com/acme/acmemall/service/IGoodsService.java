package com.acme.acmemall.service;

import com.acme.acmemall.model.GoodsVo;

import java.util.List;
import java.util.Map;

public interface IGoodsService {

    /**
     * 统计商品数量
     *
     * @param map 查询条件
     * @return 数量
     */
    int queryTotal(Map<String, Object> map);

    /**
     * 商品列表查询
     *
     * @param map 查询条件
     * @return 查询结果
     */
    List<GoodsVo> queryGoodsList(Map<String, Object> map);

    GoodsVo queryObject(long goodsId);
}
