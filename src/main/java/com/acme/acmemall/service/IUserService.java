package com.acme.acmemall.service;

import com.acme.acmemall.model.LoginUserVo;
import com.acme.acmemall.model.UserGoods;

import java.util.List;

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
}
