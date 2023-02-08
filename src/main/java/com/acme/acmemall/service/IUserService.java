package com.acme.acmemall.service;

import com.acme.acmemall.model.LoginUserVo;

/**
 * @author HP
 * @date 2023-02-08
 */
public interface IUserService {

    LoginUserVo queryByOpenId(String openId);
}
