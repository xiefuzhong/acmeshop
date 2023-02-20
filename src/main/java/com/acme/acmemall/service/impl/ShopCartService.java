package com.acme.acmemall.service.impl;

import com.acme.acmemall.dao.ShopCartMapper;
import com.acme.acmemall.model.MerCartVo;
import com.acme.acmemall.model.ShopCartVo;
import com.acme.acmemall.service.IShopCartService;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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

    /**
     * @param id
     * @return
     */
    @Override
    public ShopCartVo queryObject(Integer id) {
        return mapper.queryObject(id);
    }

    /**
     * @param id
     */
    @Override
    public void delete(Integer id) {
        mapper.delete(id);
    }

    /**
     * @param userId
     * @param productIdsArray
     */
    @Override
    public void deleteByUserAndProductIds(Long userId, String[] productIdsArray) {
        mapper.deleteByUserAndProductIds(userId,productIdsArray);
    }

    /**
     * @param userId
     * @return
     */
    @Override
    public List<MerCartVo> queryMerCartList(Long userId) {
        return mapper.queryMerCartList(userId);
    }

    /**
     * @param map
     * @return
     */
    @Override
    public List<ShopCartVo> queryCheckedByUserIdAndMerId(Map map) {
        return mapper.queryCheckedByUserIdAndMerId(map);
    }

    /**
     * @param merchantId
     * @return
     */
    @Override
    public String queryMerchantName(Long merchantId) {
        return mapper.queryMerchantName(merchantId);
    }

    /**
     * @param productIds
     * @param isChecked
     * @param userId
     */
    @Override
    public void updateCheck(String[] productIds, Integer isChecked, Long userId) {
        mapper.updateCheck(productIds, isChecked, userId);

        // 判断购物车中是否存在此规格商品
        Map cartParam = Maps.newHashMap();
        cartParam.put("user_id", userId);
        cartParam.put("checked", 1);

        List<ShopCartVo> cartInfoList = mapper.queryList(cartParam);
        List<ShopCartVo> cartUpdateList = Lists.newArrayList();
        // 获取已选择的商品ID
//        List<Integer> goods_ids = cartInfoList.stream().map(shopCartVo -> shopCartVo.getGoods_id()).collect(Collectors.toList());
        if (CollectionUtils.isEmpty(cartInfoList)) {
            return;
        }
        cartUpdateList = cartInfoList.stream().filter(item->!item.getRetail_price().equals(item.getRetail_product_price())).map(item->{
            item.setRetail_price(item.getRetail_price());
            return item;
        }).collect(Collectors.toList());

//        for (ShopCartVo cartItem : cartInfoList) {
//            // 存在原始的
//            if (null != cartItem.getChecked() && 1 == cartItem.getChecked()) {
//                for (ShopCartVo cartCrash : cartInfoList) {
//                    if (null != cartItem.getChecked() && 1 == cartItem.getChecked() && !cartCrash.getId().equals(cartItem.getId())) {
//                        cartUpdateList.add(cartCrash);
//                        break;
//                    }
//                }
//            }
//        }
        if (CollectionUtils.isEmpty(cartUpdateList)) {
            return;
        }
        mapper.batchUpdate(cartUpdateList);
    }
}
