package com.acme.acmemall.dao;

import com.acme.acmemall.model.FeedbackVo;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface FeedbackMapper extends BaseDao<FeedbackVo> {
}
