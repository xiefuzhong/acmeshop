package com.acme.acmemall.dao;

import com.acme.acmemall.model.LoginUserVo;
import com.acme.acmemall.model.UserGoods;
import com.acme.acmemall.model.UserGroup;
import com.acme.acmemall.model.UserLabel;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

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

    List<Map> findRoleByUserId(@Param("userId") Long userId);

    int updateUserGroup(@Param("userIds") String[] userIds, @Param("group") UserGroup group);

    int updateUserLabels(@Param("userIds") String[] userIds, @Param("labels") String labels);

    List<Map> countByUserId(@Param("userId") Long userId);

    void batchAddGroup(List<UserGroup> groups);

    void batchAddLabel(List<UserLabel> labels);

    List<Map> queryGroup();

    List<Map> queryLabel();
}

