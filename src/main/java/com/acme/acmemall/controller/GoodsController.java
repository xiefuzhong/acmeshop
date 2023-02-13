package com.acme.acmemall.controller;

import com.acme.acmemall.annotation.IgnoreAuth;
import com.acme.acmemall.model.CategoryVo;
import com.acme.acmemall.service.ICategoryService;
import com.acme.acmemall.service.IGoodsService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Api(tags = "商品管理")
@RestController
@RequestMapping("/api/goods")
public class GoodsController extends ApiBase {
    @Autowired
    private IGoodsService goodsService;

    @Autowired
    private ICategoryService categoryService;

    /**
     * 　　在售的商品总数
     */
    @ApiOperation(value = "在售的商品总数")
    @IgnoreAuth
    @GetMapping(value = "count")
    public Object count() {
        Map<String, Object> result = new HashMap();
        Map param = new HashMap();
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
        Map<String, Object> resultObj = new HashMap();
        CategoryVo currentCategory = categoryService.queryObject(id);
        CategoryVo parentCategory = categoryService.queryObject(currentCategory.getParentId());
        Map params = new HashMap();
        params.put("parentId", currentCategory.getParentId());
        List<CategoryVo> brotherCategory = categoryService.queryCategoryList(params);
        resultObj.put("currentCategory", currentCategory);
        resultObj.put("parentCategory", parentCategory);
        resultObj.put("brotherCategory", brotherCategory);
        return toResponsSuccess(resultObj);
    }
}
