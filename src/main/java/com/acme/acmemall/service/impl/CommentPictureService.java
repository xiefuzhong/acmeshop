package com.acme.acmemall.service.impl;

import com.acme.acmemall.model.CommentPictureVo;
import com.acme.acmemall.service.ICommentPictureService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * @description:
 * @author: ihpangzi
 * @time: 2023/2/14 16:59
 */
@Service
public class CommentPictureService implements ICommentPictureService {
    /**
     * @param id
     * @return
     */
    @Override
    public CommentPictureVo queryObject(long id) {
        return null;
    }

    /**
     * @param map
     * @return
     */
    @Override
    public List<CommentPictureVo> queryCommentPicList(Map<String, Object> map) {
        return null;
    }
}
