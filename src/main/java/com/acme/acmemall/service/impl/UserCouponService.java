package com.acme.acmemall.service.impl;

import com.acme.acmemall.dao.UserCouponMapper;
import com.acme.acmemall.model.UserCouponVo;
import com.acme.acmemall.service.IUserCouponService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * @description:
 * @author: ihpangzi
 * @time: 2023/2/14 10:31
 */
@Service
public class UserCouponService implements IUserCouponService {

    @Autowired
    UserCouponMapper userCouponMapper;

    /**
     * @param userCouponVo
     */
    @Override
    public void save(UserCouponVo userCouponVo) {
        userCouponMapper.save(userCouponVo);
    }

    /**
     * @param param
     * @return
     */
    @Override
    public List<UserCouponVo> queryUserCouponList(Map param) {
        return userCouponMapper.queryList(param);
    }

    /**
     * @param userCouponVo
     */
    @Override
    public void update(UserCouponVo userCouponVo) {
        userCouponMapper.update(userCouponVo);
    }

    /**
     * @param userParams
     * @return
     */
    @Override
    public int queryUserGetTotal(Map userParams) {
        return userCouponMapper.queryUserGetTotal(userParams);
    }

    /**
     * @param userCouponVos
     */
    @Override
    public void batchSave(List<UserCouponVo> userCouponVos) {
        userCouponMapper.saveBatch(userCouponVos);
    }
}
