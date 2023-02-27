package com.acme.acmemall.service;

import com.acme.acmemall.model.OrderGoodsVo;

import java.util.List;
import java.util.Map;

public interface IOrderGoodsService {

    public List<OrderGoodsVo> queryList(Map<String, Object> map);
}
