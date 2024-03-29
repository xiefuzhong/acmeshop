package com.acme.acmemall.service;

import com.acme.acmemall.common.ResultMap;
import com.acme.acmemall.controller.reqeust.GoodsManageRequest;
import com.acme.acmemall.controller.reqeust.GoodsSubmitRequest;
import com.acme.acmemall.model.GoodsVo;
import com.acme.acmemall.model.LoginUserVo;

import java.util.List;
import java.util.Map;

public interface IGoodsService {

    /**
     * 统计商品数量
     *
     * @param map 查询条件
     * @return 数量
     */
    int queryTotal(Map<String, Object> map);

    /**
     * 商品列表查询
     *
     * @param map 查询条件
     * @return 查询结果
     */
    List<GoodsVo> queryGoodsList(Map<String, Object> map);

    GoodsVo queryObject(long goodsId);

    /**
     * 商品详情（基本信息，产品信息，规格、收藏，浏览记录、优惠券等）
     *
     * @param goodsId
     * @param userVo  用户信息
     * @return
     */
    GoodsVo loadGoodsDetail(LoginUserVo userVo, Long goodsId);

    ResultMap submit(GoodsSubmitRequest request, LoginUserVo loginUser);

    ResultMap updateGoods(GoodsManageRequest request, LoginUserVo loginUser);

    /**
     * 查询商品详情(包含基本信息，规格，库存信息)用于编辑页面展示
     *
     * @param goodsId
     * @return
     */
    GoodsVo loadAllDetail(long goodsId);
}
