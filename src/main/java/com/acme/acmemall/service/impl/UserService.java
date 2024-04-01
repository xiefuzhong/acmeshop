package com.acme.acmemall.service.impl;

import com.acme.acmemall.dao.UserMapper;
import com.acme.acmemall.exception.ApiCusException;
import com.acme.acmemall.model.LoginUserVo;
import com.acme.acmemall.model.UserGoods;
import com.acme.acmemall.model.UserGroup;
import com.acme.acmemall.model.UserLabel;
import com.acme.acmemall.service.IUserService;
import com.acme.acmemall.utils.GsonUtil;
import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Lists;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

@Service
public class UserService implements IUserService {

    @Resource
    private UserMapper userDao;

    protected Logger logger = Logger.getLogger(getClass());


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
     * 账号登录校验
     *
     * @param mobile
     * @param password
     * @return
     */
    @Override
    public LoginUserVo login(String mobile, String password) {
        String pwd = DigestUtils.sha256Hex(password);
        LoginUserVo loginUserVo = userDao.queryByMobile(mobile, pwd);
        logger.info(GsonUtil.getGson().toJson(loginUserVo));
        if (loginUserVo == null || !loginUserVo.checkLogin(pwd)) {
            throw new ApiCusException("登录失败!");
        }
        return loginUserVo;
    }

    /**
     * @param userId
     * @return
     */
    @Override
    public LoginUserVo queryByUserId(long userId) {
        return userDao.findByUserId(userId);
    }

    /**
     * @param userId
     * @return true-admin
     */
    @Override
    public Boolean checkAdmin(long userId) {
        long count = (long) userDao.findRoleByUserId(userId).get(0).get("cnt");
        return count > 0 ? Boolean.TRUE : Boolean.FALSE;
    }

    /**
     * @param paramMap
     * @return
     */
    @Override
    public List<LoginUserVo> queryUserList(Map<String, Object> paramMap) {
        return userDao.queryList(paramMap);
    }

    /**
     * @param user
     */
    @Override
    public void updateUser(LoginUserVo user) {
        userDao.update(user);
    }

    /**
     * @param userIds
     * @param user
     */
    @Override
    public void updateUserGroup(String[] userIds, LoginUserVo user) {
        userDao.updateUserGroup(userIds, user);
    }

    /**
     * @param userId
     * @return
     */
    @Override
    public List<Map> countByUserId(long userId) {
        return userDao.countByUserId(userId);
    }

    /**
     * @param object
     */
    @Override
    public void addSet(JSONObject object) {
        String type = object.getString("type");
        JSONObject obj = object.getJSONObject("data");
        if ("group".equals(type)) {
            UserGroup group = JSONObject.toJavaObject(obj, UserGroup.class);
            userDao.batchAddGroup(Lists.newArrayList(group));
        } else {
            UserLabel label = JSONObject.toJavaObject(obj, UserLabel.class);
            userDao.batchAddLabel(Lists.newArrayList(label));
        }
    }

    /**
     * @return
     */
    @Override
    public List<Object> loadSet() {
        return null;
    }

    public void loadSet(JSONObject object) {
        String type = object.getString("type");
        JSONObject obj = object.getJSONObject("data");
        if ("group".equals(type)) {
            UserGroup group = JSONObject.toJavaObject(obj, UserGroup.class);
            userDao.batchAddGroup(Lists.newArrayList(group));
        } else {
            UserLabel label = JSONObject.toJavaObject(obj, UserLabel.class);
            userDao.batchAddLabel(Lists.newArrayList(label));
        }
    })

}
