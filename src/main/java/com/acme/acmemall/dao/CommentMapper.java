package com.acme.acmemall.dao;

import com.acme.acmemall.model.CommentVo;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface CommentMapper extends BaseDao<CommentVo> {
}
