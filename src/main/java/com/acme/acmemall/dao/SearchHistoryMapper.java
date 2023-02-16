package com.acme.acmemall.dao;

import com.acme.acmemall.model.SearchHistoryVo;
import org.apache.ibatis.annotations.Param;

public interface SearchHistoryMapper extends BaseDao<SearchHistoryVo> {
    int deleteByUserId(@Param("user_id") Long userId);
}
