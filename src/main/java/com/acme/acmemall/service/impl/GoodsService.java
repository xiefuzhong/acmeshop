package com.acme.acmemall.service.impl;

import com.acme.acmemall.service.IGoodsService;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class GoodsService implements IGoodsService {
    /**
     * 统计商品数量
     *
     * @param map 查询条件
     * @return 数量
     */
    @Override
    public int queryTotal(Map<String, Object> map) {
        return 1;
    }
}
