package com.acme.acmemall.service;

import com.acme.acmemall.model.SpecificationVo;

import java.util.List;
import java.util.Map;

public interface ISpecificationService {

    void addSpecification(SpecificationVo spec);

    List<SpecificationVo> querySpecifications(Map<String, Object> params);
}
