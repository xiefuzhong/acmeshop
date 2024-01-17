package com.acme.acmemall.dao;

import com.acme.acmemall.common.QueryParam;
import com.acme.acmemall.model.GoodsVo;

import java.util.List;
import java.util.Map;

/**
 * @author admin
 * @date 2017-08-11 09:16:45
 */
public interface GoodsMapper extends BaseDao<GoodsVo> {

    List<GoodsVo> queryMerGoodsList(Map<String, Object> params);

    List<GoodsVo> queryHotGoodsList(Map<String, Object> params);

    List<GoodsVo> queryCatalogProductList(Map<String, Object> params);

    List<GoodsVo> queryKillList();

    Integer queryMaxId();

    List<GoodsVo> queryTop4(Integer brand_id);

    List<GoodsVo> queryFxList(Map<String, Object> map);

    int queryFxTotal(QueryParam query);

    List<GoodsVo> queryGroupList(QueryParam query);

    int queryGroupTotal(QueryParam query);

    int queryKillTotal(QueryParam query);

    List<GoodsVo> queryKillPage(QueryParam query);

    GoodsVo queryGoodsDetail(Long id);

    List<GoodsVo> queryByIds(Long[] ids);

    void batchUpdate(List<GoodsVo> goodsList);
}
