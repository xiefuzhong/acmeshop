package com.acme.acmemall.service.impl;

import com.acme.acmemall.dao.SearchHistoryMapper;
import com.acme.acmemall.model.SearchHistoryVo;
import com.acme.acmemall.service.ISearchHistoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * @description:历史搜索记录
 * @author: ihpangzi
 * @time: 2023/2/16 10:16
 */
@Service
public class SearchHistoryService implements ISearchHistoryService {

    @Autowired
    SearchHistoryMapper mapper;

    /**
     * @param map
     * @return
     */
    @Override
    public List<SearchHistoryVo> querySearchHistoryList(Map<String, Object> map) {
        return mapper.queryList(map);
    }
}
