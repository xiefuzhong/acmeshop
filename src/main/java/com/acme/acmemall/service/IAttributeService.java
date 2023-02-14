package com.acme.acmemall.service;

import com.acme.acmemall.model.AttributeVo;

import java.util.List;
import java.util.Map;

public interface IAttributeService {

    List<AttributeVo> queryAttributeList(Map<String,Object> map);
}
