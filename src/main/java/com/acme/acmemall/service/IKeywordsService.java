package com.acme.acmemall.service;

import com.acme.acmemall.model.KeywordsVo;

import java.util.List;
import java.util.Map;

public interface IKeywordsService {
    KeywordsVo queryObject(Integer id);

    List<KeywordsVo> queryKeywordsList(Map<String,Object> map);

    List<Map> hotKeywordList(Map<String, Object> map);
}
