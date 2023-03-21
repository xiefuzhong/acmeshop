package com.acme.acmemall.service.impl;

import com.acme.acmemall.dao.UserMapper;
import com.acme.acmemall.model.LoginUserVo;
import com.acme.acmemall.model.UserGoods;
import com.acme.acmemall.service.IUserService;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class UserService implements IUserService {

    @Resource
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
     * @param id
     * @return
     */
    @Override
    public LoginUserVo queryObject(long id) {
        return userDao.queryObject(id);
    }

    /**
     * @param user
     */
    @Override
    public void save(LoginUserVo user) {
        userDao.save(user);
    }

    @Override
    public UserGoods queryShareGoods(UserGoods userGoods) {
        return userDao.queryShareGoods(userGoods);
    }

    /**
     * @param userGoods
     * @return
     */
    @Override
    public List<UserGoods> queryShareList(UserGoods userGoods) {
        return userDao.queryUserShareGoods(userGoods);
    }

    /**
     * @param userGoods
     * @return
     */
    @Override
    public void saveShareGoods(UserGoods userGoods) {
        userDao.saveUserGoods(userGoods);
    }

    /**
     * @param userGoods
     */
    @Override
    public int delShareGoods(UserGoods userGoods) {
        return userDao.deleteShareGoods(userGoods);
    }

    /**
     * @param mobile
     * @param password
     * @return
     */
    @Override
    public long login(String mobile, String password) {
        LoginUserVo loginUserVo = userDao.queryByMobile(mobile, DigestUtils.sha256Hex(password));

        return 0;
    }
}
