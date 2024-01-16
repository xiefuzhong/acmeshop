package com.acme.acmemall.service.impl;

import com.acme.acmemall.common.ResultMap;
import com.acme.acmemall.controller.reqeust.GoodsRequest;
import com.acme.acmemall.controller.reqeust.GoodsSubmitRequest;
import com.acme.acmemall.dao.*;
import com.acme.acmemall.exception.ResultCodeEnum;
import com.acme.acmemall.factory.GoodsFactory;
import com.acme.acmemall.model.*;
import com.acme.acmemall.service.IGoodsService;
import org.apache.commons.collections.MapUtils;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

@Service
public class GoodsService implements IGoodsService {
    protected Logger logger = Logger.getLogger(getClass());
    @Resource
    private GoodsMapper goodsDao;

    @Resource
    private UserMapper userMapper;

    @Resource
    private GoodsGalleryMapper galleryMapper;

    @Resource
    private GoodsSpecificationMapper specificationMapper;

    @Resource
    private ProductMapper productMapper;

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
        return goodsDao.queryObject(goodsId);
    }

    /**
     * @param request
     * @param loginUser
     * @return
     */
    @Transactional
    @Override
    public ResultMap submit(GoodsSubmitRequest request, LoginUserVo loginUser) {
        GoodsRequest goodsRequest = request.getGoods();
        logger.info(goodsRequest.toString());
        // 检查账号是不是管理员账号,非管理员账号拒绝操作
        LoginUserVo userVo = userMapper.queryByUserId(loginUser.getUserId(), goodsRequest.getMerchantId());
        if (userVo == null || userVo.getUserId() == 0) {
            return ResultMap.error(1001, "请先登录管理系统再操作!");
        }
        GoodsVo goodsVo = GoodsFactory.createGoods(goodsRequest);
        goodsDao.save(goodsVo);
        logger.info(goodsVo.toString());
        List<GoodsGalleryVo> galleryVoList = request.getGalleryList();
        galleryVoList.stream().forEach(galleryVo -> {
            galleryVo.setGoods_id(goodsVo.getId());
        });
        goodsVo.relatedDetails("gallery", galleryVoList);
        galleryMapper.saveBatch(request.getGalleryList());

        List<GoodsSpecificationVo> specifications = request.getSpecList();
        specifications.stream().forEach(spec -> {
            spec.setGoods_id(goodsVo.getId());
        });

        specificationMapper.saveBatch(specifications);

        List<ProductVo> products = request.getProducts();
        products.stream().forEach(productVo -> {
            productVo.setGoods_id(goodsVo.getId());
        });
        productMapper.saveBatch(products);

        return ResultMap.response(ResultCodeEnum.SUCCESS, goodsVo);
    }
}
