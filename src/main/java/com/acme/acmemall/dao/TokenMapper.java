package com.acme.acmemall.dao;

import com.acme.acmemall.model.Token;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface TokenMapper extends BaseDao<Token> {
    /**
     * 查询用户token
     * @param userId
     * @return
     */
    Token queryByUserId(@Param("userId") Long userId);
}
