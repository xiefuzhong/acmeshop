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

    List<GoodsVo> queryGoodsList(Map<String, Object> map);
}
