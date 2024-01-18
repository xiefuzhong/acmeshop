package com.acme.acmemall.controller;

import com.acme.acmemall.model.ProductVo;
import com.acme.acmemall.service.IProductService;
import com.google.common.collect.Maps;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

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
    public Object getProduct(@RequestParam("goods_id") Long goodsId) {
        // 产品查询
        Map paramMap = Maps.newHashMap();
        paramMap.put("goods_id", goodsId);
        List<ProductVo> productList = productService.queryProductList(paramMap);
        return toResponsSuccess(productList);
    }
}
