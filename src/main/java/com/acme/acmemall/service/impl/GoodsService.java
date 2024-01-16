package com.acme.acmemall.service.impl;

import com.acme.acmemall.common.ResultMap;
import com.acme.acmemall.controller.reqeust.GoodsSubmitRequest;
import com.acme.acmemall.dao.GoodsGalleryMapper;
import com.acme.acmemall.dao.GoodsMapper;
import com.acme.acmemall.dao.UserMapper;
import com.acme.acmemall.exception.ResultCodeEnum;
import com.acme.acmemall.model.GoodsGalleryVo;
import com.acme.acmemall.model.GoodsVo;
import com.acme.acmemall.model.LoginUserVo;
import com.acme.acmemall.service.IGoodsService;
import com.acme.acmemall.utils.GsonUtil;
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
        GoodsVo goodsVo = request.getGoods();
        logger.info(goodsVo.toString());
        // 检查账号是不是管理员账号,非管理员账号拒绝操作
        LoginUserVo userVo = userMapper.queryByUserId(loginUser.getUserId(), goodsVo.getMerchantId());
        if (userVo == null || userVo.getUserId() == 0) {
            return ResultMap.error(1001, "请先登录管理系统再操作!");
        }

        goodsDao.save(goodsVo);
//        long goodsId = goodsVo.getId();
        logger.info(goodsVo.toString());
        List<GoodsGalleryVo> galleryVoList = request.getGalleryList();
        galleryVoList.stream().forEach(galleryVo -> {
            galleryVo.setGoods_id(goodsVo.getId());
        });
        logger.info(GsonUtil.getGson().toJson(galleryVoList));
        galleryMapper.saveBatch(request.getGalleryList());

        return ResultMap.response(ResultCodeEnum.SUCCESS, goodsVo);
    }
}
