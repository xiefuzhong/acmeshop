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
    LoginUserVo queryByMobile(@Param("mobile") String mobile, @Param("password") String password);

    /**
     * w微信用户查询
     * @param openId
     * @return
     */
    LoginUserVo queryByOpenId(@Param("openId") String openId);

    LoginUserVo queryByUserId(@Param("userId") Long userId, @Param("merchantId") Long merchantId);

    LoginUserVo findByUserId(@Param("userId") Long userId);


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

    int findRoleByUserId(@Param("userId") Long userId);
}

