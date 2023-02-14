package com.acme.acmemall.service;

import com.acme.acmemall.model.BrandVo;

import java.util.List;
import java.util.Map;

public interface IBrandService {
    BrandVo queryObject(Integer id);

    List<BrandVo> queryList(Map<String, Object> map);
}
