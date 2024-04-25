/*
 * Copyright (c) 2024. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */

package com.acme.acmemall.service.impl;

import com.acme.acmemall.dao.CapitalFlowMapper;
import com.acme.acmemall.model.CapitalFlowVo;
import com.acme.acmemall.service.IFinanceFowService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * @description:
 * @author: ihpangzi
 * @time: 2024/4/25 18:01
 */

@Service
public class FinanceFowServiceImpl implements IFinanceFowService {

    @Resource
    CapitalFlowMapper capitalFlowMapper;

    /**
     * Get capital flow data
     *
     * @param params
     * @return
     */
    @Override
    public List<CapitalFlowVo> getFinanceFlowList(Map<String, Object> params) {
        // TODO: get capital flow data from database
        List<CapitalFlowVo> capitalFlowList = capitalFlowMapper.queryList(params);
        return null;
    }
}
