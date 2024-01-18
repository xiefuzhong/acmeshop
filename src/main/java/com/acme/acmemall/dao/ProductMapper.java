package com.acme.acmemall.dao;

import com.acme.acmemall.model.ProductVo;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface ProductMapper extends BaseDao<ProductVo>{

    void batchUpdate(List<ProductVo> productVoList);
}
