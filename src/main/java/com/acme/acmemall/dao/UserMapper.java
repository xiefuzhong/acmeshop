package com.acme.acmemall.dao;

import com.acme.acmemall.model.LoginUserVo;
import com.acme.acmemall.model.UserGoods;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 用户
 */
@Mapper
public interface UserMapper extends BaseDao<LoginUserVo> {
    LoginUserVo queryByMobile(String mobile);

    /**
     * w微信用户查询
     * @param openId
     * @return
     */
    LoginUserVo queryByOpenId(@Param("openId") String openId);

    /**
     * 更新分销比例
     * @param vo
     * @return
     */
    int updatefx(LoginUserVo vo);

    UserGoods queryShareGoods(UserGoods userGoods);

    List<UserGoods> queryUserShareGoods(UserGoods userGoods);

    void saveUserGoods(UserGoods userGoods);

    int deleteShareGoods(UserGoods userGoods);
}

