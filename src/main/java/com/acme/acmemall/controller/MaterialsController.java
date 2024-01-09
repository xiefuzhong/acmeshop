package com.acme.acmemall.controller;

import com.acme.acmemall.annotation.LoginUser;
import com.acme.acmemall.common.ResultMap;
import com.acme.acmemall.exception.ResultCodeEnum;
import com.acme.acmemall.model.LoginUserVo;
import com.acme.acmemall.model.ProductMaterialsVo;
import com.acme.acmemall.service.IProductMaterialService;
import com.acme.acmemall.utils.PageUtils;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @description:店铺管理-素材管理
 * @author: ihpangzi
 * @time: 2024/1/9 14:21
 */
@RestController
@RequestMapping("/api/manage/materials")
public class MaterialsController extends ApiBase {
    @Autowired
    IProductMaterialService productMaterialsService;

    @GetMapping("list")
    public Object list(@LoginUser LoginUserVo userVo, @RequestParam(value = "page", defaultValue = "1") Integer page,
                       @RequestParam(value = "size", defaultValue = "10") Integer size) {
        Map param = new HashMap();
        param.put("page", page);
        param.put("limit", size);
        param.put("sidx", "id");
        param.put("order", "desc");
//        param.put("fields", "id, title, price_info, scene_pic_url,subtitle");
        //查询列表数据
        PageHelper.startPage(page, size);
        List<ProductMaterialsVo> topicList = productMaterialsService.queryMaterialsList(param);
        PageUtils pageUtil = new PageUtils(new PageInfo(topicList));
        return ResultMap.response(ResultCodeEnum.SUCCESS, pageUtil);
    }

    @PostMapping("save")
    public Object save(@LoginUser LoginUserVo userVo) {
        JSONObject requestObj = super.getJsonRequest();
        if (null != requestObj) {
            JSONArray array = requestObj.getJSONArray("fileList");
            List<ProductMaterialsVo> fileList = JSONArray.parseArray(array.toJSONString(), ProductMaterialsVo.class);
            productMaterialsService.batchSave(fileList);
        }
        return ResultMap.response(ResultCodeEnum.SUCCESS);
    }

}
