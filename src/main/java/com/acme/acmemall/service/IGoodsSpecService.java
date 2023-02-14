package com.acme.acmemall.service;

import com.acme.acmemall.model.GoodsSpecificationVo;

import java.util.List;
import java.util.Map;

/**
 * 商品规格接口
 */
public interface IGoodsSpecService {
    GoodsSpecificationVo queryObject(Integer id);

    List<GoodsSpecificationVo> queryGoodsSpecList(Map<String,Object> map);
}
