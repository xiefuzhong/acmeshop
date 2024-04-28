package com.acme.acmemall.dao;

import com.acme.acmemall.model.CapitalFlowVo;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface CapitalFlowMapper extends BaseDao<CapitalFlowVo> {
    List<Map<String, Object>> statistics(Map<String, Object> params);
}
