package com.acme.acmemall.dao;

import com.acme.acmemall.model.*;
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
     *
     * @param openId
     * @return
     */
    LoginUserVo queryByOpenId(@Param("openId") String openId);

    LoginUserVo queryByUserId(@Param("userId") Long userId, @Param("merchantId") Long merchantId);

    LoginUserVo findByUserId(@Param("userId") Long userId);


    /**
     * 更新分销比例
     *
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

    int batchAddGroup(List<UserGroup> groups);

    int batchAddLabel(List<UserLabel> labels);

    List<UserGroup> queryGroup();

    List<UserLabel> queryLabel();

    int batchDeleteGroup(List<Long> id);

    int batchDeleteLabel(List<Long> id);

    List<MembersVo> querySysMembers(Map<String, Object> params);

    int addSysMembers(MembersVo membersVo);

    int updateSysMembers(MembersVo membersVo);

    int deleteSysMembers(Long id);

    int addSysMember(MembersVo membersVo);

    MembersVo querySysMemberById(Long userId);
}

