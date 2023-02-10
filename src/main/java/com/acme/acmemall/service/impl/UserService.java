package com.acme.acmemall.service.impl;

import com.acme.acmemall.model.LoginUserVo;
import com.acme.acmemall.service.IUserService;
import org.springframework.stereotype.Service;

@Service
public class UserService implements IUserService {
    /**
     * @param openId
     * @return
     */
    @Override
    public LoginUserVo queryByOpenId(String openId) {
        return null;
    }

    /**
     * @param user
     */
    @Override
    public void save(LoginUserVo user) {

    }
}
