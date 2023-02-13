package com.acme.acmemall.controller;

import com.acme.acmemall.annotation.IgnoreAuth;
import com.acme.acmemall.model.*;
import com.acme.acmemall.service.*;
import com.github.pagehelper.PageHelper;
import com.google.common.collect.Maps;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 首页入口
 *
 * @author IHPANGZI
 * @date 2023-02-08
 */
@Api(tags = "商城首页")
@RestController
@RequestMapping("/api/index")
public class IndexController extends ApiBase {
    @Autowired
    private IAdService adService;

    @Autowired
    private IChannelService channelService;

    @Autowired
    private ICategoryService categoryService;

    @Autowired
    private IGoodsService goodsService;

    @Autowired
    private ITopicService topicService;

    /**
     * 主页页面
     *
     * @return API response html
     */
    @GetMapping
    public String index() {
        return "index";
    }

    @ApiOperation(value = "banner")
    @IgnoreAuth
    @GetMapping(value = "banner")
    public Object banner() {
        Map<String, Object> resultObj = Maps.newHashMap();
        Map<String, Object> param = Maps.newHashMap();
        param.put("adPositionId", 1);
        List<AdVo> banner = adService.queryAdList(param);
        resultObj.put("banner", banner);
        return toResponsSuccess(resultObj);
    }

    @ApiOperation(value = "channel")
    @IgnoreAuth
    @GetMapping(value = "channel")
    public Object channel() {
        Map<String, Object> resultMap = Maps.newHashMap();
        Map<String, Object> param = Maps.newHashMap();
        param.put("sidx", "sortOrder ");
        param.put("order", "asc ");
        List<ChannelVo> channel = channelService.queryChannelList(param);
        resultMap.put("channel", channel);
        return toResponsSuccess(resultMap);
    }

    @ApiOperation(value = "category")
    @IgnoreAuth
    @GetMapping(value = "category")
    public Object category() {
        Map<String, Object> resultMap = Maps.newHashMap();
        Map<String, Object> param = Maps.newHashMap();
        param.put("parentId", 0);
        param.put("notName", "推荐");
        List<CategoryVo> categoryList = categoryService.queryCategoryList(param);
        List<Map<String, Object>> newCategoryList = new ArrayList<>();

        for (CategoryVo categoryItem : categoryList) {
            param.remove("fields");
            param.put("parentId", categoryItem.getId());
            List<CategoryVo> categoryEntityList = categoryService.queryCategoryList(param);
            List<Integer> childCategoryIds = null;
            if (categoryEntityList != null && categoryEntityList.size() > 0) {
                childCategoryIds = new ArrayList<>();
                for (CategoryVo categoryEntity : categoryEntityList) {
                    childCategoryIds.add(categoryEntity.getId());
                }
            }
            if (childCategoryIds.isEmpty()) {
                logger.info("parentId：" + categoryItem.getId() + " sub ids is null");
                continue;
            }
            param = Maps.newHashMap();
            param.put("categoryIds", childCategoryIds);
            param.put("fields", "id as id, name as name, list_pic_url as list_pic_url, retail_price as retail_price");
            param.put("is_delete", 0);
            param.put("is_on_sale", 1);
            PageHelper.startPage(0, 7, false);
            List<GoodsVo> categoryGoods = goodsService.queryGoodsList(param);
            Map<String, Object> newCategory = Maps.newHashMap();
            newCategory.put("id", categoryItem.getId());
            newCategory.put("name", categoryItem.getName());
            newCategory.put("goodsList", categoryGoods);
            newCategoryList.add(newCategory);
        }
        resultMap.put("categoryList", newCategoryList);
        return toResponsSuccess(resultMap);
    }

    /**
     * app首页-新品信息
     */
    @ApiOperation(value = "新品信息")
    @IgnoreAuth
    @GetMapping(value = "newGoods")
    public Object newGoods() {
        Map<String, Object> resultObj = Maps.newHashMap();
        Map<String, Object> param = Maps.newHashMap();
        param.put("is_new", 1);
        param.put("is_delete", 0);
        param.put("is_on_sale", 1);
        // 自定义查询返回字段
        // param.put("fields", "id, name, list_pic_url, retail_price");
        PageHelper.startPage(0, 4, false);
        List<GoodsVo> newGoods = goodsService.queryGoodsList(param);
        resultObj.put("newGoodsList", newGoods);
        return toResponsSuccess(resultObj);
    }

    @ApiOperation(value = "爆款信息")
    @IgnoreAuth
    @GetMapping(value = "hotGoods")
    public Object hotGoods() {
        Map<String, Object> resultObj = Maps.newHashMap();
        Map<String, Object> param = Maps.newHashMap();
        param.put("is_hot", "1"); // 热门
        param.put("is_delete", 0);
        param.put("is_on_sale", 1); // 在售商品
        PageHelper.startPage(0, 3, false);
        List<GoodsVo> hotGoods = goodsService.queryGoodsList(param);
        resultObj.put("hotGoodsList", hotGoods);
        return toResponsSuccess(resultObj);
    }

    @ApiOperation(value = "topic")
    @IgnoreAuth
    @GetMapping(value = "topic")
    public Object topic() {
        Map<String, Object> resultObj = Maps.newHashMap();
        Map<String, Object> param = Maps.newHashMap();
        param.put("offset", 0);
        param.put("limit", 3);
        List<TopicVo> topicList = topicService.queryTopicList(param);
        resultObj.put("topicList", topicList);
        return toResponsSuccess(resultObj);
    }
}
