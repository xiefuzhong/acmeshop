package com.acme.acmemall.service;

import com.acme.acmemall.model.LoginUserVo;
import com.acme.acmemall.model.UserGoods;
import com.alibaba.fastjson.JSONObject;

import java.util.List;
import java.util.Map;

/**
 * @author HP
 * @date 2023-02-08
 */
public interface IUserService {

    LoginUserVo queryByOpenId(String openId);

    LoginUserVo queryObject(long id);

    void save(LoginUserVo user);

    UserGoods queryShareGoods(UserGoods userGoods);

    List<UserGoods> queryShareList(UserGoods userGoods);

    void saveShareGoods(UserGoods userGoods);

    int delShareGoods(UserGoods userGoods);

    LoginUserVo login(String mobile, String password);

    LoginUserVo queryByUserId(long userId);

    Boolean checkAdmin(long userId);

    List<LoginUserVo> queryUserList(Map<String, Object> paramMap);

    void updateUser(LoginUserVo user);

    void updateUserGroup(String[] userIds, LoginUserVo user);

    List<Map> countByUserId(long userId);

    void addSet(JSONObject object);
}
