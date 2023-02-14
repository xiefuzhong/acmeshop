package com.acme.acmemall.service.impl;

import com.acme.acmemall.dao.GoodsSpecificationMapper;
import com.acme.acmemall.model.GoodsSpecificationVo;
import com.acme.acmemall.service.IGoodsSpecService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * @description: 商品规格信息服务
 * @author: ihpangzi
 * @time: 2023/2/13 18:06
 */
@Service
public class GoodsSpecService implements IGoodsSpecService {
    @Autowired
    private GoodsSpecificationMapper mapper;

    /**
     * @param id
     * @return
     */
    @Override
    public GoodsSpecificationVo queryObject(Integer id) {
        return mapper.queryObject(id);
    }

    /**
     * @param map
     * @return
     */
    @Override
    public List<GoodsSpecificationVo> queryGoodsSpecList(Map<String, Object> map) {
        return mapper.queryList(map);
    }
}
