package com.acme.acmemall.dao;

import com.acme.acmemall.model.OrderVo;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface OrderMapper extends BaseDao<OrderVo>{
}
