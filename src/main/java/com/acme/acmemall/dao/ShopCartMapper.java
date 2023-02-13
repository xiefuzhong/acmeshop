package com.acme.acmemall.dao;

import com.acme.acmemall.model.MerCartVo;
import com.acme.acmemall.model.ShopCartVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * @author admin
 *
 * @date 2017-08-11 09:14:25
 */
@Mapper
public interface ShopCartMapper extends BaseDao<ShopCartVo> {
    void updateCheck(@Param("productIds") String[] productIds,
                     @Param("isChecked") Integer isChecked, @Param("userId") Long userId);

    void deleteByProductIds(@Param("productIds") String[] productIds);

    void deleteByUserAndProductIds(@Param("user_id") Long user_id,@Param("productIds") String[] productIds);

    void deleteByCart(@Param("user_id") Long user_id, @Param("session_id") Integer session_id, @Param("checked") Integer checked);

    List<ShopCartVo> queryMrchantGroup(Map<String, Object> map);

    List<MerCartVo> queryMerCartList(Long userId);

    List<ShopCartVo> queryCheckedByUserIdAndMerId(Map map);

    String queryMerchantName(Long merchantId);
}
