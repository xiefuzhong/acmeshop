package com.acme.acmemall.service;

import com.acme.acmemall.model.ProductMaterialsVo;

import java.util.List;
import java.util.Map;

public interface IProductMaterialService {
    List<ProductMaterialsVo> queryMaterialsList(Map params);

    void batchSave(List<ProductMaterialsVo> entityList);

    void batchDelete(Long[] id);
}
