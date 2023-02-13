package com.acme.acmemall.service.impl;

import com.acme.acmemall.dao.CouponMapper;
import com.acme.acmemall.model.CouponVo;
import com.acme.acmemall.service.ICouponService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * @description:优惠券
 * @author: ihpangzi
 * @time: 2023/2/13 12:02
 */
@Service
public class CouponService implements ICouponService {

    @Autowired
    CouponMapper couponDao;

    @Autowired

    /**
     * @param map
     * @return
     */
    @Override
    public List<CouponVo> queryCouponList(Map<String, Object> map) {
        return couponDao.queryList(map);
    }
}
