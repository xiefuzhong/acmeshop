package com.acme.acmemall.service.impl;

import com.acme.acmemall.dao.SpecificationMapper;
import com.acme.acmemall.model.SpecificationVo;
import com.acme.acmemall.service.ISpecificationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * @description:
 * @author: ihpangzi
 * @time: 2024/1/25 12:21
 */
@Service
@Slf4j
public class SpecificationService implements ISpecificationService {

    @Resource
    private SpecificationMapper specificationMapper;

    /**
     * @param spec
     */
    @Override
    public void addSpecification(SpecificationVo spec) {
        specificationMapper.save(spec);
    }

    /**
     * @param params
     * @return
     */
    @Override
    public List<SpecificationVo> querySpecifications(Map<String, Object> params) {
        return specificationMapper.queryList(params);
    }
}
