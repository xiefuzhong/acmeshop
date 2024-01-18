package com.acme.acmemall.service.impl;

import com.acme.acmemall.dao.ProductMapper;
import com.acme.acmemall.model.ProductVo;
import com.acme.acmemall.service.IProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * @description:
 * @author: ihpangzi
 * @time: 2023/2/13 18:08
 */
@Service
public class ProductService implements IProductService {

    @Autowired
    ProductMapper productMapper;
    /**
     * @param map
     * @return
     */
    @Override
    public List<ProductVo> queryProductList(Map<String, Object> map) {
        return productMapper.queryList(map);
    }

    /**
     * @param productId
     * @return
     */
    @Override
    public ProductVo queryObject(Integer productId) {
        return productMapper.queryObject(productId);
    }

    /**
     * @param entityList
     */
    @Override
    public void batchUpdate(List<ProductVo> entityList) {
        productMapper.batchUpdate(entityList);
    }
}
