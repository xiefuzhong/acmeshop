package com.acme.acmemall.service.impl;

import com.acme.acmemall.dao.CommentMapper;
import com.acme.acmemall.model.CommentVo;
import com.acme.acmemall.service.ICommentService;
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
        return mapper.queryList(map);
    }

    /**
     * @param commentVo
     */
    @Override
    public int doSave(CommentVo commentVo) {
        return mapper.save(commentVo);
    }
}
