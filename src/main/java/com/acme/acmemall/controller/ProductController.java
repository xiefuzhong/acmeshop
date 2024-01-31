package com.acme.acmemall.controller;

import com.acme.acmemall.annotation.LoginUser;
import com.acme.acmemall.common.ResultMap;
import com.acme.acmemall.model.LoginUserVo;
import com.acme.acmemall.model.ProductVo;
import com.acme.acmemall.service.IProductService;
import com.acme.acmemall.utils.GsonUtil;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.google.common.base.Throwables;
import com.google.common.collect.Maps;
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
        JSONObject parameters = super.getJsonRequest();
        if (parameters == null) {
            ResultMap.badArgument();
        }
        JSONArray array = parameters.getJSONArray("products");
        List<ProductVo> productVoList = JSONArray.parseArray(array.toJSONString(), ProductVo.class);
        try {
            logger.info("productVoList==>" + GsonUtil.toJson(productVoList));
            productService.batchUpdate(productVoList);
        } catch (Exception e) {
            String errMsg = Throwables.getStackTraceAsString(e);
            return ResultMap.error("update failed!,error details >> " + errMsg);
        }
        return ResultMap.ok();
    }
}
