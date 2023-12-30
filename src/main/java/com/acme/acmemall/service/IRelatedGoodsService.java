package com.acme.acmemall.service;

import com.acme.acmemall.model.RelatedGoodsVo;

import java.util.List;
import java.util.Map;

public interface IRelatedGoodsService {

    RelatedGoodsVo queryObject(Integer id);

    List<RelatedGoodsVo> queryList(Map<String, Object> paramMap);
}
