package com.acme.acmemall.service;

import com.acme.acmemall.model.CapitalFlowVo;

import java.util.List;
import java.util.Map;

public interface IFinanceFowService {

    int saveCapitalFlow(CapitalFlowVo capitalFlowVo);

    /**
     * Get capital flow data
     *
     * @param params
     * @return
     */
    List<CapitalFlowVo> getFinanceFlowList(Map<String, Object> params);

    List<Map<String, Object>> statistics(Map<String, Object> params);
}
