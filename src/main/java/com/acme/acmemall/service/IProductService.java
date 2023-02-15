package com.acme.acmemall.service;

import com.acme.acmemall.model.ProductVo;

import java.util.List;
import java.util.Map;

public interface IProductService {
    List<ProductVo> queryProductList(Map<String,Object> map);

    ProductVo queryObject(Integer productId);
}
