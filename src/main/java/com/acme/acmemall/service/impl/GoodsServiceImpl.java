package com.acme.acmemall.service.impl;

import com.acme.acmemall.common.GoodsHandleType;
import com.acme.acmemall.common.ResultMap;
import com.acme.acmemall.controller.reqeust.GoodsManageRequest;
import com.acme.acmemall.controller.reqeust.GoodsRequest;
import com.acme.acmemall.controller.reqeust.GoodsSubmitRequest;
import com.acme.acmemall.dao.*;
import com.acme.acmemall.exception.ResultCodeEnum;
import com.acme.acmemall.factory.GoodsFactory;
import com.acme.acmemall.model.*;
import com.acme.acmemall.service.IGoodsService;
import com.acme.acmemall.utils.GsonUtil;
import com.acme.acmemall.utils.StringUtils;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * @author xfz
 */
@Service
public class GoodsServiceImpl implements IGoodsService {
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

    private static final String HOT_KEY = "is_hot";
    private static final String HOT_VALUE = "1";
    private static final String NEW_KEY = "is_new";
    private static final String NEW_VALUE = "1";

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
        if (HOT_VALUE.equals(MapUtils.getString(map, HOT_KEY))) {
            // 热门商品查询
            return goodsDao.queryHotGoodsList(map);
        } else if (NEW_VALUE.equals(MapUtils.getString(map, NEW_KEY))) {
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
    @Transactional(rollbackFor = {RuntimeException.class, Exception.class})
    @Override
    public ResultMap submit(GoodsSubmitRequest request, LoginUserVo loginUser) {
        GoodsRequest goodsRequest = request.getGoods();

        // 检查账号是不是管理员账号,非管理员账号拒绝操作
        LoginUserVo userVo = userMapper.queryByUserId(loginUser.getUserId(), goodsRequest.getMerchantId());
        if (userVo == null || userVo.getUserId() == 0) {
            return ResultMap.error(1001, "请先登录管理系统再操作!");
        }
        GoodsVo goodsVo = GoodsFactory.createGoods(goodsRequest, userVo);
        Optional<ProductVo> optionalProductVo = request.getProducts().stream().filter(product -> product.getGoods_number() > 0).findFirst();
        if (optionalProductVo.isPresent()) {
            goodsVo.updateStock(optionalProductVo.get());
        }
        goodsDao.save(goodsVo);
        logger.info("save goodsVo after ==>" + goodsVo.toString());
        List<GoodsGalleryVo> galleryVoList = request.getGalleryList();
        galleryVoList.stream().forEach(galleryVo -> {
            galleryVo.setGoods_id(goodsVo.getId());
        });
        logger.info("galleryVoList==>" + GsonUtil.getGson().toJson(galleryVoList));
        goodsVo.relatedDetails("gallery", galleryVoList);
        galleryMapper.saveBatch(request.getGalleryList());

        List<GoodsSpecificationVo> specifications = request.getSpecList();
        specifications.stream().forEach(spec -> {
            spec.setGoods_id(goodsVo.getId());
        });
        logger.info("specifications==>" + GsonUtil.getGson().toJson(specifications));
        goodsVo.relatedDetails("spec", specifications);
        specificationMapper.saveBatch(specifications);

        List<ProductVo> products = request.getProducts();
        products.stream().forEach(productVo -> {
            productVo.setGoods_id(goodsVo.getId());
        });
        logger.info("products==>" + GsonUtil.getGson().toJson(products));
        goodsVo.relatedDetails("product", products);
        productMapper.saveBatch(products);

        return ResultMap.response(ResultCodeEnum.SUCCESS, goodsVo);
    }

    @Transactional(rollbackFor = {RuntimeException.class, Exception.class})
    @Override
    public ResultMap updateGoods(GoodsManageRequest request, LoginUserVo loginUserVo) {
        if (StringUtils.isNullOrEmpty(request.getGoodsIds())) {
            return ResultMap.badArgument();
        }
        Long[] goodsIds = (Long[]) Arrays.stream(request.getGoodsIds().split(",")).toArray();
        // 操作
        String handle = request.getHandle();
        List<GoodsVo> goodsList = goodsDao.queryByIds(goodsIds);
        if (CollectionUtils.isEmpty(goodsList)) {
            return ResultMap.badArgument();
        }
        if (!GoodsHandleType.validate(handle)) {
            return ResultMap.badArgument();
        }
        goodsList.stream().forEach(goodsVo -> goodsVo.update(GoodsHandleType.parse(handle), request));

        return ResultMap.response(ResultCodeEnum.SUCCESS);
    }
}
