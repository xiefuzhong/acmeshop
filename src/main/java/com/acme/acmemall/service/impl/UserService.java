package com.acme.acmemall.service.impl;

import com.acme.acmemall.dao.UserMapper;
import com.acme.acmemall.model.LoginUserVo;
import com.acme.acmemall.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService implements IUserService {

    @Autowired
    private UserMapper userDao;
    /**
     * @param openId
     * @return
     */
    @Override
    public LoginUserVo queryByOpenId(String openId) {
        return userDao.queryByOpenId(openId);
    }

    /**
     * @param user
     */
    @Override
    public void save(LoginUserVo user) {
        userDao.save(user);
    }
}
