package com.acme.acmemall.service;

import java.util.Map;

public interface IGoodsService {

    /**
     * 统计商品数量
     *
     * @param map 查询条件
     * @return 数量
     */
    int queryTotal(Map<String, Object> map);
}
