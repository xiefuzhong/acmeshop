package com.acme.acmemall.controller;

import com.acme.acmemall.annotation.LoginUser;
import com.acme.acmemall.common.ResultMap;
import com.acme.acmemall.model.LoginUserVo;
import com.acme.acmemall.model.ProductVo;
import com.acme.acmemall.service.IProductService;
import com.acme.acmemall.service.IUserService;
import com.acme.acmemall.utils.GsonUtil;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.google.common.base.Throwables;
import com.google.common.collect.Maps;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * @description: SKU
 * @author: ihpangzi
 * @time: 2024/1/18 15:56
 */
@RestController
@RequestMapping("/api/product")
public class ProductController extends ApiBase {

    @Resource
    IProductService productService;

    @Resource
    IUserService userService;

    @GetMapping("/list")
    public Object getProduct(@LoginUser LoginUserVo loginUser, @RequestParam("goods_id") Long goodsId) {
        // 产品查询
        Map paramMap = Maps.newHashMap();
        paramMap.put("goods_id", goodsId);
        List<ProductVo> productList = productService.queryProductList(paramMap);
        return toResponsSuccess(productList);
    }

    @PostMapping("/update")
    public Object update(@LoginUser LoginUserVo loginUser) {
        if (loginUser == null) {
            return ResultMap.error("未登录");
        }
        if (!userService.checkAdmin(loginUser.getUserId())) {
            return ResultMap.error("没有权限");
        }
        JSONObject jsonRequest = super.getJsonRequest();
        if (jsonRequest == null) {
            ResultMap.badArgument();
        }
        logger.info("batchUpdate.before == jsonRequest ==>" + jsonRequest.toJSONString());
        String handle = jsonRequest.getString("handle");
        JSONArray array = jsonRequest.getJSONArray("products");
        List<ProductVo> productVoList = JSONArray.parseArray(array.toJSONString(), ProductVo.class);

        if (CollectionUtils.isEmpty(productVoList)) {
            return ResultMap.badArgument("参数错误");
        }
        try {
            productService.batchUpdate(productVoList);
            logger.info("batchUpdate.after == productVoList ==>" + GsonUtil.toJson(productVoList));
        } catch (Exception e) {
            String errMsg = Throwables.getStackTraceAsString(e);
            return ResultMap.error("update failed!,error details >> " + errMsg);
        }
        return ResultMap.ok();
    }
}
