package com.acme.acmemall.service;

import com.acme.acmemall.model.CommentVo;

import java.util.List;
import java.util.Map;

public interface ICommentService {

    Integer queryTotal(Map<String, Object> map);

    List<CommentVo> queryCommentList(Map<String, Object> map);

    int doSave(CommentVo commentVo);

    CommentVo queryComment(Long id);

    int updateComment(CommentVo commentVo);
}
