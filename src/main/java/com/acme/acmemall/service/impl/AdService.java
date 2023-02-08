package com.acme.acmemall.service.impl;

import com.acme.acmemall.dao.AdMapper;
import com.acme.acmemall.model.AdVo;
import com.acme.acmemall.service.IAdService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class AdService implements IAdService {

    @Autowired
    private AdMapper adDao;
    /**
     * 查询bannerList
     *
     * @param map
     * @return
     */
    @Override
    public List<AdVo> queryAdList(Map<String, Object> map) {
        return adDao.queryList(map);
    }
}
