package com.acme.acmemall.service;

import com.acme.acmemall.model.CommentPictureVo;

import java.util.List;
import java.util.Map;

public interface ICommentPictureService {
    CommentPictureVo queryObject(long id);

    List<CommentPictureVo> queryCommentPicList(Map<String,Object> map);
}
