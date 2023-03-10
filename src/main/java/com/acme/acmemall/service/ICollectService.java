package com.acme.acmemall.service;

import com.acme.acmemall.model.CollectVo;

import java.util.List;
import java.util.Map;

public interface ICollectService {
    Integer queryTotal(Map<String,Object> map);

    List<CollectVo> queryList(Map param);

    Integer save(CollectVo collectEntity);

    Integer delete(Integer id);

    Integer saveOrUpdate(CollectVo collect);
}
