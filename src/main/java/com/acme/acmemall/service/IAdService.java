package com.acme.acmemall.service;

import com.acme.acmemall.model.AdVo;

import java.util.List;
import java.util.Map;

public interface IAdService {

    /**
     * 查询bannerList
     * @param map
     * @return
     */
    List<AdVo> queryAdList(Map<String, Object> map);
}
