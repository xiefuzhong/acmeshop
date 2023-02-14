package com.acme.acmemall.service.impl;

import com.acme.acmemall.model.CommentVo;
import com.acme.acmemall.service.ICommentService;
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
    /**
     * @param map
     * @return
     */
    @Override
    public Integer queryTotal(Map<String, Object> map) {
        return null;
    }

    /**
     * @param map
     * @return
     */
    @Override
    public List<CommentVo> queryCommentList(Map<String, Object> map) {
        return null;
    }
}
