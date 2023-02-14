package com.acme.acmemall.service.impl;

import com.acme.acmemall.dao.GoodsIssueMapper;
import com.acme.acmemall.model.GoodsIssueVo;
import com.acme.acmemall.service.IGoodsIssueService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * @description:
 * @author: ihpangzi
 * @time: 2023/2/14 16:39
 */
@Service
public class GoodsIssueService implements IGoodsIssueService {
    private GoodsIssueMapper mapper;

    @Autowired
    public GoodsIssueService(GoodsIssueMapper mapper){
        this.mapper = mapper;
    };
    /**
     * @param id
     * @return
     */
    @Override
    public GoodsIssueVo queryObject(long id) {
        return mapper.queryObject(id);
    }

    /**
     * @param paramMap
     * @return
     */
    @Override
    public List<GoodsIssueVo> queryIssueList(Map<String, Object> paramMap) {
        return mapper.queryList(paramMap);
    }
}
