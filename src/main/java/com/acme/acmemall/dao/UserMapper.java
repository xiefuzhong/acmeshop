package com.acme.acmemall.dao;

import com.acme.acmemall.model.LoginUserVo;
import org.apache.ibatis.annotations.Param;

/**
 * 用户
 *
 * @author admin
 *
 * @date 2017-03-23 15:22:06
 */
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

}

