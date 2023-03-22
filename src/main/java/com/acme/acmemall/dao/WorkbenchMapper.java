package com.acme.acmemall.dao;

import com.acme.acmemall.model.StatisticsVo;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface WorkbenchMapper extends BaseDao<StatisticsVo> {

    List<StatisticsVo> statistics(Map<String, Object> params);
}
