package com.acme.acmemall.service;

import com.acme.acmemall.model.StatisticsVo;

import java.util.List;
import java.util.Map;

public interface IWorkbenchService {
    List<StatisticsVo> statistics(Map<String, Object> params);
}
