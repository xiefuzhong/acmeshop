package com.acme.acmemall.service.impl;

import com.acme.acmemall.model.FootprintVo;
import com.acme.acmemall.service.IFootprintService;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * @description:
 * @author: ihpangzi
 * @time: 2023/2/14 10:26
 */
@Service
public class FootprintService implements IFootprintService {
    /**
     * @param footprintVo
     */
    @Override
    public void save(FootprintVo footprintVo) {

    }

    /**
     * @param map
     * @return
     */
    @Override
    public Integer queryTotal(Map<String, Object> map) {
        return null;
    }
}
