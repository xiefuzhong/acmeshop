package com.acme.acmemall.service;

import com.acme.acmemall.model.UserCouponVo;

import java.util.List;
import java.util.Map;

public interface IUserCouponService {
    void save(UserCouponVo userCouponVo);

    List<UserCouponVo> queryUserCouponList(Map param);

    void update(UserCouponVo userCouponVo);

    int queryUserGetTotal(Map userParams);

    void batchSave(List<UserCouponVo> userCouponVos);
}
