package com.acme.acmemall.service.impl;

import com.acme.acmemall.dao.ShopCartMapper;
import com.acme.acmemall.model.ShopCartVo;
import com.acme.acmemall.service.IShopCartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * @description:购物车功能
 * @author: ihpangzi
 * @time: 2023/2/13 11:32
 */
@Service
public class ShopCartService implements IShopCartService {

    @Autowired
    private ShopCartMapper mapper;

    /**
     * 购物车查询
     *
     * @param map 查询条件
     * @return 购物车清单
     */
    @Override
    public List<ShopCartVo> queryCartList(Map<String, Object> map) {
        return this.mapper.queryList(map);
    }

    /**
     * @param cartInfo
     */
    @Override
    public void save(ShopCartVo cartInfo) {
        mapper.save(cartInfo);
    }

    /**
     * @param cartInfo
     */
    @Override
    public void update(ShopCartVo cartInfo) {
        mapper.update(cartInfo);
    }
}
