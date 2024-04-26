package com.acme.acmemall.factory;

import com.acme.acmemall.model.CapitalFlowVo;

/**
 * @description:
 * @author: ihpangzi
 * @time: 2024/4/26 15:18
 */
public class FinanceFlowFatroy {

    public static CapitalFlowVo createFinanceFlow(Integer type) {
        return CapitalFlowVo.builder().trade_type(type).build();
    }
}
