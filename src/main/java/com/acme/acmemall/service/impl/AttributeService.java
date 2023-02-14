package com.acme.acmemall.service.impl;

import com.acme.acmemall.model.AttributeVo;
import com.acme.acmemall.service.IAttributeService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * @description:
 * @author: ihpangzi
 * @time: 2023/2/14 9:39
 */
@Service
public class AttributeService implements IAttributeService {
    /**
     * @param map
     * @return
     */
    @Override
    public List<AttributeVo> queryAttributeList(Map<String, Object> map) {
        return null;
    }
}
