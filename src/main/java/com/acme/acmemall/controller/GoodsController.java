package com.acme.acmemall.controller;

import com.acme.acmemall.annotation.IgnoreAuth;
import com.acme.acmemall.annotation.LoginUser;
import com.acme.acmemall.common.ResultMap;
import com.acme.acmemall.controller.reqeust.GoodsManageRequest;
import com.acme.acmemall.controller.reqeust.GoodsSubmitRequest;
import com.acme.acmemall.exception.ResultCodeEnum;
import com.acme.acmemall.model.*;
import com.acme.acmemall.service.*;
import com.acme.acmemall.utils.DateUtils;
import com.acme.acmemall.utils.GsonUtil;
import com.acme.acmemall.utils.PageUtils;
import com.acme.acmemall.utils.StringUtils;
import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Api(tags = "商品管理")
@RestController
@RequestMapping("/api/goods")
public class GoodsController extends ApiBase {

    private final IAdService adService;
    private final IBrandService brandService;
    private final IGoodsService goodsService;
    private final ICategoryService categoryService;
    private final IUserService userService;
    private final IGoodsSpecService goodsSpecService;
    private final IProductService productService;
    private final IGoodsGalleryService galleryService;
    private final ICouponService couponService;
    private final IAttributeService attributeService;
    private final IGoodsIssueService issueService;
    private final ICommentService commentService;
    private final ICommentPictureService pictureService;
    private final ICollectService collectService;
    private final IFootprintService footprintService;
    private final ISearchHistoryService searchHistoryService;

    private final IRelatedGoodsService relatedGoodsService;

    @Autowired
    public GoodsController(ISearchHistoryService searchHistoryService,
                           IUserService userService,
                           IBrandService brandService,
                           IGoodsService goodsService,
                           ICategoryService categoryService,
                           IAdService adService,
                           IGoodsSpecService goodsSpecService,
                           IProductService productService,
                           IFootprintService footprintService,
                           ICollectService collectService,
                           IGoodsIssueService issueService,
                           IAttributeService attributeService,
                           ICouponService couponService,
                           IGoodsGalleryService galleryService,
                           ICommentPictureService pictureService,
                           IRelatedGoodsService relatedGoodsService,
                           ICommentService commentService) {
        this.pictureService = pictureService;
        this.categoryService = categoryService;
        this.goodsService = goodsService;
        this.brandService = brandService;
        this.adService = adService;
        this.userService = userService;
        this.goodsSpecService = goodsSpecService;
        this.productService = productService;
        this.searchHistoryService = searchHistoryService;
        this.footprintService = footprintService;
        this.collectService = collectService;
        this.commentService = commentService;
        this.issueService = issueService;
        this.attributeService = attributeService;
        this.couponService = couponService;
        this.galleryService = galleryService;
        this.relatedGoodsService = relatedGoodsService;
    }

    /**
     * 　　在售的商品总数
     */
    @ApiOperation(value = "在售的商品总数")
    @IgnoreAuth
    @GetMapping(value = "count")
    public Object count() {
        Map<String, Object> result = Maps.newHashMap();
        Map param = Maps.newHashMap();
        param.put("is_delete", 0);
        param.put("is_on_sale", 1);
        Integer goodsCount = goodsService.queryTotal(param);
        result.put("goodsCount", goodsCount);
        return toResponsSuccess(result);
    }

    /**
     * 　获取分类下的商品
     */
    @ApiOperation(value = " 获取分类下的商品")
    @ApiImplicitParams({@ApiImplicitParam(name = "id", value = "分类id", paramType = "path", required = true)})
    @IgnoreAuth
    @GetMapping(value = "category")
    public Object category(Integer id) {
        Map<String, Object> resultObj = Maps.newHashMap();
        CategoryVo currentCategory = categoryService.queryObject(id);
        CategoryVo parentCategory = categoryService.queryObject(currentCategory.getParentId());
        Map params = Maps.newHashMap();
        params.put("parentId", currentCategory.getParentId());
        List<CategoryVo> brotherCategory = categoryService.queryCategoryList(params);
        resultObj.put("currentCategory", currentCategory);
        resultObj.put("parentCategory", parentCategory);
        resultObj.put("brotherCategory", brotherCategory);
        return toResponsSuccess(resultObj);
    }

    /**
     * 　　获取商品列表
     */
    @ApiOperation(value = "获取商品列表")
    @ApiImplicitParams({@ApiImplicitParam(name = "categoryId", value = "分类id", paramType = "path", required = true),
            @ApiImplicitParam(name = "brandId", value = "品牌Id", paramType = "path", required = true),
            @ApiImplicitParam(name = "isNew", value = "新商品", paramType = "path", required = true),
            @ApiImplicitParam(name = "isHot", value = "热卖商品", paramType = "path", required = true)})
    @IgnoreAuth
    @GetMapping(value = "list")
    public Object list(@LoginUser LoginUserVo loginUser, Integer categoryId,
                       Integer brandId, String keyword, Integer isNew, Integer isHot,
                       @RequestParam(value = "page", defaultValue = "1") Integer page,
                       @RequestParam(value = "size", defaultValue = "10") Integer size,
                       String sort, String order) {
        Map params = Maps.newHashMap();
        params.put("is_delete", 0);
        params.put("is_on_sale", 1);
        params.put("brand_id", brandId);
        params.put("keyword", keyword);
        params.put("is_new", isNew);
        params.put("is_hot", isHot);
        params.put("page", page);
        params.put("limit", size);
        params.put("order", sort);
        params.put("sidx", order);
        //
        if ("price".equals(sort)) {
            params.put("sidx", "retail_price");
            params.put("order", order);
        } else {
            params.put("sidx", "id");
            params.put("order", "desc");
        }
        // 添加到搜索历史
        if (!StringUtils.isNullOrEmpty(keyword)) {
            SearchHistoryVo searchHistoryVo = new SearchHistoryVo();
            searchHistoryVo.setAdd_time(System.currentTimeMillis() / 1000);
            searchHistoryVo.setKeyword(keyword);
            searchHistoryVo.setUser_id(null != loginUser ? loginUser.getUserId().toString() : "");
            searchHistoryVo.setFrom("");
            searchHistoryService.save(searchHistoryVo);

        }

        //加入分类条件
        if (null != categoryId && categoryId > 0) {
            List<Integer> categoryIds = Lists.newArrayList();
            Map categoryParam = Maps.newHashMap();
            categoryParam.put("parentId", categoryId);
            categoryParam.put("fields", "id");
            List<CategoryVo> childIds = categoryService.queryCategoryList(categoryParam);
            for (CategoryVo categoryEntity : childIds) {
                categoryIds.add(categoryEntity.getId());
            }
            categoryIds.add(categoryId);
            params.put("categoryIds", categoryIds);
        }
        //查询列表数据
        params.put("fields", "id, name, list_pic_url, market_price, retail_price, goods_brief,is_service,short_link");
//        QueryParam query = new QueryParam(params);
        PageHelper.startPage(page, size);
        List<GoodsVo> goodsList = goodsService.queryGoodsList(params);
        PageUtils goodsData = new PageUtils(new PageInfo(goodsList));
        //搜索到的商品
        goodsData.setGoodsList(goodsList);
        return toResponsSuccess(goodsData);
    }

    /**
     * 商品详情页数据
     *
     * @param userVo   登录账号信息
     * @param id       商品ID
     * @param referrer 商品referrer
     * @return
     */
    @ApiOperation(value = " 商品详情页数据")
    @IgnoreAuth
    @ApiImplicitParams({@ApiImplicitParam(name = "id", value = "商品id", paramType = "path", required = true),
            @ApiImplicitParam(name = "referrer", value = "商品referrer", paramType = "path", required = false)})
    @GetMapping(value = "detail")
    public Object detail(@LoginUser LoginUserVo userVo, Integer id, Long referrer) {
        logger.info("goods.detail===============================>" + id);
        Map<String, Object> resultObj = Maps.newHashMap();
        Long userId = getUserId();
        if (userVo != null) {
            userId = userVo.getUserId();
        } else {
            logger.info("访客浏览");
        }

        //MlsUserEntity2 loginUser = mlsUserSer.getEntityMapper().findByUserId(userId);
        GoodsVo info = goodsService.queryObject(id);
        //info.setDiscount(info.getRetail_price().multiply(new BigDecimal("10")).divide(info.getMarket_price(), 1, BigDecimal.ROUND_HALF_UP).toString());
        Long mid = info.getMerchantId();
//        Map<String, Object> sysuser = this.mlsUserSer.getEntityMapper().getSysUserByMid(mid);
        // 佣金计算
//        info.setUser_brokerage_price(info.getRetail_price().multiply(new BigDecimal(sysuser.get("FX").toString())).multiply(new BigDecimal(info.getBrokerage_percent()).divide(new BigDecimal("10000"))).setScale(2, BigDecimal.ROUND_HALF_UP).toString());

        //
        Map specParamMap = Maps.newHashMap();
        specParamMap.put("fields", "gs.*, s.name");
        specParamMap.put("goods_id", id);
        specParamMap.put("enabled", 1);
        specParamMap.put("specification", true);
//        specificationParam.put("sidx", "s.sort_order");
        specParamMap.put("order", "asc");
        // 商品规格信息
        List<GoodsSpecificationVo> goodsSpecList = goodsSpecService.queryGoodsSpecList(specParamMap);

        List<Map> specList = Lists.newArrayList();
        //按规格名称分组
        for (int i = 0; i < goodsSpecList.size(); i++) {
            GoodsSpecificationVo specItem = goodsSpecList.get(i);
            //
            List<GoodsSpecificationVo> tempSpecList = null;
            for (int j = 0; j < specList.size(); j++) {
                if (specList.get(j).get("specification_id").equals(specItem.getSpecification_id())) {
                    tempSpecList = (List<GoodsSpecificationVo>) specList.get(j).get("valueList");
                    break;
                }
            }
            //
            if (null == tempSpecList) {
                Map temp = Maps.newHashMap();
                temp.put("specification_id", specItem.getSpecification_id());
                temp.put("name", specItem.getName());
                temp.put("pic_url", specItem.getPic_url());
                tempSpecList = Lists.newArrayList();
                tempSpecList.add(specItem);
                temp.put("valueList", tempSpecList);
                specList.add(temp);
            } else {
                for (int j = 0; j < specList.size(); j++) {
                    if (specList.get(j).get("specification_id").equals(specItem.getSpecification_id())) {
                        tempSpecList = (List<GoodsSpecificationVo>) specList.get(j).get("valueList");
                        tempSpecList.add(specItem);
                        break;
                    }
                }
            }
        }
        // 产品查询
        Map paramMap = Maps.newHashMap();
        paramMap.put("goods_id", id);
        List<ProductVo> productList = productService.queryProductList(paramMap);

        // 关联产品查询
        paramMap.clear();
        paramMap.put("sidx", "id");
        paramMap.put("order", "asc");
        paramMap.put("goods_id", id);
        paramMap.put("type", 2);
        List<RelatedGoodsVo> relatedGoods = relatedGoodsService.queryList(paramMap);
        List<Integer> relatedGoodIds = relatedGoods.stream().map(RelatedGoodsVo::getRelated_goods_id).collect(Collectors.toList());

        List<GoodsVo> relatedGoodsList = Lists.newArrayList();
        if (!relatedGoodIds.isEmpty()) {
            // 配件列表信息
            paramMap.clear();
            paramMap.put("fields", "id, name, list_pic_url, market_price, retail_price, goods_brief,is_service,short_link");
            paramMap.put("goods_ids", relatedGoodIds);
            relatedGoodsList = goodsService.queryGoodsList(paramMap);
        }
        // 产品主图,倒序
        paramMap.clear();
        paramMap.put("sidx", "id");
        paramMap.put("order", "desc");
        paramMap.put("goods_id", id);
        paramMap.put("enabled", 1);
        List<GoodsGalleryVo> gallery = galleryService.queryGoodsGalleryList(paramMap);

        // 产品规格
        Map ngaParam = Maps.newHashMap();
        ngaParam.put("fields", "nga.value, na.name");
        ngaParam.put("sidx", "nga.id");
        ngaParam.put("order", "asc");
        ngaParam.put("goods_id", id);
        List<AttributeVo> attribute = attributeService.queryAttributeList(ngaParam);
        //
        Map issueParam = Maps.newHashMap();
//        issueParam.put("goods_id", id);
        List<GoodsIssueVo> issue = issueService.queryIssueList(issueParam);
        //
        BrandVo brand = brandService.queryObject(info.getBrand_id());
        //
        paramMap.clear();
        paramMap.put("value_id", id);
        paramMap.put("type_id", 0);
        Integer commentCount = commentService.queryTotal(paramMap);
        List<CommentVo> commentList = commentService.queryCommentList(paramMap);
        CommentVo commentVo = commentList.stream().findFirst().orElse(CommentVo.builder().build());
        Map commentInfo = Maps.newHashMap();
        if (CollectionUtils.isNotEmpty(commentList)) {
            LoginUserVo commentUser = userService.queryObject(commentVo.getUser_id());
            commentInfo.put("content", commentVo.getContent());
            commentInfo.put("add_time", DateUtils.timeToUtcDate(commentVo.getAdd_time(), DateUtils.DATE_TIME_PATTERN));
            commentInfo.put("nickname", commentUser.getNickname());
            commentInfo.put("avatar", commentUser.getAvatar());
            Map paramPicture = Maps.newHashMap();
            paramPicture.put("comment_id", commentVo.getId());
            List<CommentPictureVo> picList = pictureService.queryCommentPicList(paramPicture);
            commentInfo.put("pic_list", picList);
        }
        Map comment = Maps.newHashMap();
        comment.put("count", commentCount);
        comment.put("data", commentInfo);
        //当前用户是否收藏
        Map collectParam = Maps.newHashMap();
        collectParam.put("user_id", getUserId());
        collectParam.put("value_id", id);
        collectParam.put("type_id", 0);
        Integer userHasCollect = collectService.queryTotal(collectParam);
        if (userHasCollect > 0) {
            userHasCollect = 1;
        }
//        Integer userHasCollect = 1;
        //记录用户的足迹
        FootprintVo foot = FootprintVo.builder().user_id(userId).build();
        foot.record(info, referrer);
        footprintService.save(foot);
        //
        resultObj.put("info", info);
        resultObj.put("gallery", gallery);
        resultObj.put("attribute", attribute);
        resultObj.put("userHasCollect", userHasCollect);
        resultObj.put("issue", issue);
        resultObj.put("comment", comment);
        resultObj.put("brand", brand);
        resultObj.put("specificationList", specList);
        resultObj.put("productList", productList);
        resultObj.put("relatedGoodsList", relatedGoodsList);
        // 记录推荐人是否可以领取红包，用户登录时校验
//        try {
//            // 是否已经有可用的转发红包
//            Map params = Maps.newHashMap();
//            params.put("user_id", userId);
//            params.put("send_type", 2);
//            params.put("unUsed", true);
//            List<CouponVo> enabledCouponVos = couponService.queryUserCoupons(params);
//            if ((null == enabledCouponVos || enabledCouponVos.size() == 0)
//                    && null != referrer && null != userId) {
//                // 获取优惠信息提示
//                Map couponParam = Maps.newHashMap();
//                couponParam.put("enabled", true);
//                Integer[] send_types = new Integer[]{2};
//                couponParam.put("send_types", send_types);
//                List<CouponVo> couponVos = couponService.queryCouponList(couponParam);
//                if (null != couponVos && couponVos.size() > 0) {
//                    CouponVo couponVo = couponVos.get(0);
//                    Map footprintParam = Maps.newHashMap();
//                    footprintParam.put("goods_id", id);
//                    footprintParam.put("referrer", referrer);
//                    Integer footprintNum = footprintService.queryTotal(footprintParam);
//                    if (null != footprintNum && null != couponVo.getMin_transmit_num()
//                            && footprintNum > couponVo.getMin_transmit_num()) {
//                        UserCouponVo userCouponVo = new UserCouponVo();
//                        userCouponVo.setAdd_time(new Date());
//                        userCouponVo.setCoupon_id(couponVo.getId());
//                        userCouponVo.setCoupon_number(CharUtil.getRandomString(12));
//                        userCouponVo.setUser_id(getUserId());
//                        userCouponService.save(userCouponVo);
//                    }
//                }
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }

        return toResponsSuccess(resultObj);
    }

    /**
     * 　　人气推荐
     */
    @ApiOperation(value = "人气推荐")
    @IgnoreAuth
    @GetMapping(value = "hot")
    public Object hot() {
        Map<String, Object> resultObj = Maps.newHashMap();
        Map bannerInfo = Maps.newHashMap();
        bannerInfo.put("url", "");
        bannerInfo.put("name", "阿可美专业电推剪");
        bannerInfo.put("img_url", "https://platform-wxmall.oss-cn-beijing.aliyuncs.com/upload/20180727/1504208321fef4.png");
        resultObj.put("bannerInfo", bannerInfo);
        Map<String, Object> param = Maps.newHashMap();
        param.put("adPositionId", 1);
        AdVo banner = adService.queryAdList(param).get(0);

        return ResultMap.ok(resultObj);
    }

    @ApiOperation(value = "商家商品查询")
    @GetMapping(value = "list-mer")
    public Object listMerGoods(@LoginUser LoginUserVo loginUserVo,
                               @RequestParam("on_sale") Integer on_sale,
                               @RequestParam("merchant_id") Integer merchant_id,
                               @RequestParam("keyword") String keyword,
                               @RequestParam("page") Integer page,
                               @RequestParam("size") Integer size,
                               @RequestParam("catalogId") Integer catalogId) {
        Map params = Maps.newHashMap();
        params.put("is_delete", 0);
        params.put("is_on_sale", on_sale);
        params.put("keyword", keyword);
        params.put("merchant_id", merchant_id);
        if (catalogId > 0) {
            // 商品分类
            List<Integer> categoryIds = Lists.newArrayList();
            categoryIds.add(catalogId);
            params.put("categoryIds", categoryIds);
        }
        params.put("page", page);
        params.put("limit", size);
        // 根据上架时间排序
        params.put("order", "desc");
        params.put("sidx", "add_time");
        logger.info("listMerGoods ==>" + GsonUtil.toJson(params));
        PageHelper.startPage(page, size, true);
        List<GoodsVo> goods = goodsService.queryGoodsList(params);
        PageInfo pageInfo = new PageInfo(goods);
        PageUtils pager = new PageUtils(pageInfo);
        return ResultMap.response(ResultCodeEnum.SUCCESS, pager);
    }

    @PostMapping("/add")
    public Object addGoods(@LoginUser LoginUserVo loginUserVo) {
        JSONObject jsonRequest = super.getJsonRequest();
        logger.info("【请求开始】商品管理->添加商品->提交,请求参数:" + jsonRequest.toJSONString());
        if (jsonRequest == null) {
            return ResultMap.error();
        }
        GoodsSubmitRequest submitRequest = JSONObject.toJavaObject(jsonRequest, GoodsSubmitRequest.class);
        return goodsService.submit(submitRequest, loginUserVo);
    }

    /**
     * 商品管理功能：上架/下架/草稿编辑更新/修改价格/修改数量
     *
     * @param loginUserVo
     * @return
     */
    @PostMapping("/manage")
    public Object updateGoods(@LoginUser LoginUserVo loginUserVo) {
        JSONObject jsonRequest = super.getJsonRequest();
        logger.info("【请求开始】商品管理->商品管理->提交,请求参数:" + jsonRequest.toJSONString());
        if (jsonRequest == null) {
            return ResultMap.error();
        }
        GoodsManageRequest manageRequest = JSONObject.toJavaObject(jsonRequest, GoodsManageRequest.class);
        return goodsService.updateGoods(manageRequest, loginUserVo);
    }

    @GetMapping("/manage-detail")
    public Object goodsDetailMer(@LoginUser LoginUserVo userVo, @RequestParam("goodsId") Integer goodsId) {
        GoodsVo goodsVo = goodsService.loadAllDetail(goodsId);
        return toResponsSuccess(goodsVo);
    }

}
