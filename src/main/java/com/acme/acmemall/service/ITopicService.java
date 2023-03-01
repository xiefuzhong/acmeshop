package com.acme.acmemall.service;

import com.acme.acmemall.model.TopicVo;

import java.util.List;
import java.util.Map;

public interface ITopicService {
    /**
     * 专题列表查询
     * @param map 查询参数
     * @return 专题列表
     */
    List<TopicVo> queryTopicList(Map<String, Object> map);

    /**
     * 专题详情
     * @param id
     * @return
     */
    TopicVo queryObject(Integer id);
}
