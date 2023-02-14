package com.acme.acmemall.dao;

import com.acme.acmemall.model.ProductVo;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface ProductMapper extends BaseDao<ProductVo>{
}
