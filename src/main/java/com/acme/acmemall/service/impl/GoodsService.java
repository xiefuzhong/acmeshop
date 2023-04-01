package com.acme.acmemall.service.impl;

import com.acme.acmemall.dao.GoodsMapper;
import com.acme.acmemall.model.GoodsVo;
import com.acme.acmemall.model.LoginUserVo;
import com.acme.acmemall.service.IGoodsService;
import org.apache.commons.collections.MapUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

@Service
public class GoodsService implements IGoodsService {
    @Resource
    private GoodsMapper goodsDao;

    /**
     * 统计在售商品数量
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
        if ("1".equals(MapUtils.getString(map, "is_hot"))) {
            // 热门商品查询
            return goodsDao.queryHotGoodsList(map);
        } else if ("1".equals(MapUtils.getString(map, "is_new"))) {
            // 新品收发查询
            return goodsDao.queryList(map);
        }
        return goodsDao.queryList(map);
    }

    /**
     * 商品详情查询
     *
     * @param goodsId ID
     * @return 商品详情
     */
    @Override
    public GoodsVo queryObject(long goodsId) {
        return goodsDao.queryObject(goodsId);
    }

    /**
     * 商品详情（基本信息，产品信息，规格、收藏，浏览记录、优惠券等）
     *
     * @param userVo  用户信息
     * @param goodsId
     * @return
     */
    @Override
    public GoodsVo loadGoodsDetail(LoginUserVo userVo, Long goodsId) {
        GoodsVo goodsVo = goodsDao.queryObject(goodsId);
        return null;
    }
}
