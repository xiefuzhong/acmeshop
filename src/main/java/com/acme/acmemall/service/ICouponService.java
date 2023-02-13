package com.acme.acmemall.service;

import com.acme.acmemall.model.CouponVo;

import java.util.List;
import java.util.Map;

public interface ICouponService {
    List<CouponVo> queryCouponList(Map<String,Object> map);
}
