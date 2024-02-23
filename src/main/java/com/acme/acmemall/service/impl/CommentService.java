package com.acme.acmemall.service.impl;

import com.acme.acmemall.dao.CommentMapper;
import com.acme.acmemall.dao.UserMapper;
import com.acme.acmemall.model.CommentVo;
import com.acme.acmemall.model.LoginUserVo;
import com.acme.acmemall.service.ICommentService;
import com.google.common.collect.Maps;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * @description:
 * @author: ihpangzi
 * @time: 2023/2/14 9:39
 */
@Service
public class CommentService implements ICommentService {

    @Autowired
    CommentMapper mapper;

    @Autowired
    UserMapper userMapper;

    /**
     * @param map
     * @return
     */
    @Override
    public Integer queryTotal(Map<String, Object> map) {
        return mapper.queryTotal(map);
    }

    /**
     * @param map
     * @return
     */
    @Override
    public List<CommentVo> queryCommentList(Map<String, Object> map) {
        List<CommentVo> comments = mapper.queryList(map);
        comments.stream().forEach(item -> {
            LoginUserVo user = userMapper.queryObject(item.getUser_id());
            item.resetShow(user);
        });
        return comments;
    }

    /**
     * @param commentVo
     */
    @Override
    public int doSave(CommentVo commentVo) {
        Map<String, Object> params = Maps.newHashMap();
        params.put("user_id", commentVo.getUser_id());
        params.put("value_id", commentVo.getValue_id());
        params.put("type_id", commentVo.getType_id());
        params.put("order_id", commentVo.getOrder_id());
        int count = mapper.queryTotal(params);
        if (count > 0) {
            return 0;
        }
        return mapper.save(commentVo);
    }
}
