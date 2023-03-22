package com.acme.acmemall.service.impl;

import com.acme.acmemall.dao.OrderMapper;
import com.acme.acmemall.model.StatisticsVo;
import com.acme.acmemall.service.IWorkbenchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * @description:
 * @author: ihpangzi
 * @time: 2023/3/22 14:49
 */
@Service
public class WorkbenchService implements IWorkbenchService {

    private final OrderMapper mapper;

    @Autowired
    public WorkbenchService(OrderMapper mapper) {
        this.mapper = mapper;
    }

    /**
     * 统计
     *
     * @param params
     * @return
     */
    @Override
    public List<StatisticsVo> statistics(Map<String, Object> params) {
        return mapper.statistics(params);
    }
}
