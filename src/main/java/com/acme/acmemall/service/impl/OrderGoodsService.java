package com.acme.acmemall.service.impl;

import com.acme.acmemall.dao.OrderGoodsMapper;
import com.acme.acmemall.model.OrderGoodsVo;
import com.acme.acmemall.service.IOrderGoodsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * @description:
 * @author: ihpangzi
 * @time: 2023/2/27 13:58
 */
@Service
public class OrderGoodsService implements IOrderGoodsService {

    @Autowired
    OrderGoodsMapper mapper;
    /**
     * @param map
     * @return
     */
    @Override
    public List<OrderGoodsVo> queryList(Map<String, Object> map) {
        return mapper.queryList(map);
    }
}
