package com.acme.acmemall.service.impl;

import com.acme.acmemall.dao.GoodsMapper;
import com.acme.acmemall.model.GoodsVo;
import com.acme.acmemall.service.IGoodsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class GoodsService implements IGoodsService {
    @Autowired
    private GoodsMapper goodsDao;

    /**
     * 统计商品数量
     *
     * @param map 查询条件
     * @return 数量
     */
    @Override
    public int queryTotal(Map<String, Object> map) {
        return goodsDao.queryTotal(map);
    }

    /**
     * @param map
     * @return
     */
    @Override
    public List<GoodsVo> queryGoodsList(Map<String, Object> map) {
        return goodsDao.queryList(map);
    }
}
