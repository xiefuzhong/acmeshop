package com.acme.acmemall.service;

import com.acme.acmemall.model.CapitalFlowVo;

import java.util.List;
import java.util.Map;

public interface IFinanceFowService {

    /**
     * Get capital flow data
     *
     * @param params
     * @return
     */
    List<CapitalFlowVo> getFinanceFlowList(Map<String, Object> params);
}
