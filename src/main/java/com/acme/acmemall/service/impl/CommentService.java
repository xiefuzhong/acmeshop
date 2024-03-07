package com.acme.acmemall.service.impl;

import com.acme.acmemall.dao.CommentMapper;
import com.acme.acmemall.dao.OrderMapper;
import com.acme.acmemall.dao.UserMapper;
import com.acme.acmemall.model.CommentVo;
import com.acme.acmemall.model.OrderVo;
import com.acme.acmemall.service.ICommentService;
import com.google.common.collect.Maps;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * @description:
 * @author: ihpangzi
 * @time: 2023/2/14 9:39
 */
@Service
public class CommentService implements ICommentService {


    @Resource
    CommentMapper mapper;

    @Resource
    UserMapper userMapper;

    @Resource
    OrderMapper orderMapper;

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
//            LoginUserVo user = userMapper.queryObject(item.getUser_id());
            item.resetShow(null);
        });
        return comments;
    }

    /**
     * @param commentVo
     */
    @Transactional(rollbackFor = {RuntimeException.class, Exception.class})
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
        OrderVo orderVo = orderMapper.queryObject(commentVo.getOrder_id());
        orderVo.comment();
        orderMapper.update(orderVo);
        return mapper.save(commentVo);
    }

    /**
     * @param id
     * @return
     */
    @Override
    public CommentVo queryComment(Long id) {
        return mapper.queryObject(id);
    }

    /**
     * @param commentVo
     * @return
     */
    @Override
    public int updateComment(CommentVo commentVo) {
        return mapper.update(commentVo);
    }

}
