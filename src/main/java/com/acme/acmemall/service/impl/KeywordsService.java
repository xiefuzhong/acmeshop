package com.acme.acmemall.service.impl;

import com.acme.acmemall.dao.KeywordsMapper;
import com.acme.acmemall.model.KeywordsVo;
import com.acme.acmemall.service.IKeywordsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * @description: 关键搜索词服务
 * @author: ihpangzi
 * @time: 2023/2/16 9:45
 */
@Service
public class KeywordsService implements IKeywordsService {

    @Autowired
    KeywordsMapper mapper;

    /**
     * @param id
     * @return
     */
    @Override
    public KeywordsVo queryObject(Integer id) {
        return mapper.queryObject(id);
    }

    /**
     * @param map
     * @return
     */
    @Override
    public List<KeywordsVo> queryKeywordsList(Map<String, Object> map) {
        return mapper.queryList(map);
    }

    /**
     * @param map
     * @return
     */
    @Override
    public List<Map> hotKeywordList(Map<String, Object> map) {
        return mapper.hotKeywordList(map);
    }
}
