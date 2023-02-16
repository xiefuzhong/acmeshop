package com.acme.acmemall.service;

import com.acme.acmemall.model.SearchHistoryVo;

import java.util.List;
import java.util.Map;

public interface ISearchHistoryService {

    List<SearchHistoryVo> querySearchHistoryList(Map<String,Object> map);

    void deleteByUserId(Long userId);

    void save(SearchHistoryVo searchHistoryVo);
}
