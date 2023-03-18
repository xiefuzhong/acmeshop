package com.acme.acmemall.service;

import com.acme.acmemall.model.FootprintVo;

import java.util.List;
import java.util.Map;

public interface IFootprintService {

    void save(FootprintVo footprintVo);

    Integer queryTotal(Map<String, Object> map);

    FootprintVo queryObject(Integer footprintId);

    void deleteByParam(Map<String, Object> param);

    List<FootprintVo> queryListFootprint(String s);
}
