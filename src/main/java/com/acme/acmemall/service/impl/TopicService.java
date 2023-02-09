package com.acme.acmemall.service.impl;

import com.acme.acmemall.dao.TopicMapper;
import com.acme.acmemall.model.TopicVo;
import com.acme.acmemall.service.ITopicService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
@Service
public class TopicService implements ITopicService {

    @Autowired
    private TopicMapper topicDao;
    /**
     * 专题列表查询
     *
     * @param map 查询参数
     * @return 专题列表
     */
    @Override
    public List<TopicVo> queryTopicList(Map<String, Object> map) {
        return topicDao.queryList(map);
    }
}
