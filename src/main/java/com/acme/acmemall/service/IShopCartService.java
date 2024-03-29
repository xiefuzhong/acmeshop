package com.acme.acmemall.service;

import com.acme.acmemall.model.MerCartVo;
import com.acme.acmemall.model.ShopCartVo;

import java.util.List;
import java.util.Map;

public interface IShopCartService {

    /**
     * 购物车查询
     *
     * @param map 查询条件
     * @return 购物车清单
     */
    List<ShopCartVo> queryCartList(Map<String, Object> map);

    void save(ShopCartVo cartInfo);

    void update(ShopCartVo cartInfo);

    ShopCartVo queryObject(Integer id);

    void delete(Integer id);

    void deleteByUserAndProductIds(Long userId, String[] productIdsArray);

    List<MerCartVo> queryMerCartList(Long userId);

    List<ShopCartVo> queryCheckedByUserIdAndMerId(Map map);

    String queryMerchantName(Long merchantId);

    void updateCheck(String[] productIdArray, Integer isChecked, Long userId);

    void deleteCartByUserId(Long userId);
}
