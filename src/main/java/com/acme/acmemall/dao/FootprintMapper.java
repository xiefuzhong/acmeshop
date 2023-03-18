package com.acme.acmemall.dao;

import com.acme.acmemall.model.FootprintVo;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface FootprintMapper extends BaseDao<FootprintVo> {
    int deleteByParam(Map<String, Object> map);

    List<FootprintVo> shareList(Map<String, Object> map);

    List<FootprintVo> queryListFootprint(String userid);
}
