package com.acme.acmemall.service;

import com.acme.acmemall.model.FootprintVo;

import java.util.Map;

public interface IFootprintService {

    void save(FootprintVo footprintVo);

    Integer queryTotal(Map<String,Object> map);
}
