package com.acme.acmemall.service.impl;

import com.acme.acmemall.dao.ProductMaterialMapper;
import com.acme.acmemall.model.ProductMaterialsVo;
import com.acme.acmemall.service.IProductMaterialService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * @description:
 * @author: ihpangzi
 * @time: 2024/1/9 14:39
 */
@Service
public class ProductMaterialService implements IProductMaterialService {

    protected Logger logger = Logger.getLogger(getClass());

    @Autowired
    private ProductMaterialMapper productMaterialsMapper;

    /**
     * @param params
     * @return
     */
    @Override
    public List<ProductMaterialsVo> queryMaterialsList(Map params) {
        return productMaterialsMapper.queryList(params);
    }

    /**
     * @param entityList
     * @return
     */
    @Override
    public void batchSave(List<ProductMaterialsVo> entityList) {
        productMaterialsMapper.saveBatch(entityList);
    }

    /**
     * @param id
     */
    @Override
    public void batchDelete(Long[] id) {
        productMaterialsMapper.deleteBatch(id);
    }


}
