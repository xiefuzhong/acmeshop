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
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

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
                       @RequestParam(value = "size", defaultValue = "10") Integer size,
                       @RequestParam("groupType") Integer groupType) {
        Map param = Maps.newHashMap();
        param.put("page", page);
        param.put("limit", size);
        param.put("sidx", "id");
        param.put("order", "desc");
        if (groupType > 1) {
            param.put("groupType", groupType);
        }

//        param.put("fields", "id, title, price_info, scene_pic_url,subtitle");
        //查询列表数据
        PageHelper.startPage(page, size);
        List<ProductMaterialsVo> topicList = productMaterialsService.queryMaterialsList(param);
        PageUtils pageUtil = new PageUtils(new PageInfo(topicList));
        return ResultMap.response(ResultCodeEnum.SUCCESS, pageUtil);
    }

    @PostMapping("list")
    public Object list(@LoginUser LoginUserVo userVo) {
        JSONObject requestObj = super.getJsonRequest();
        JSONArray array = requestObj.getJSONArray("materialIds");
        List<Long> ids = array.toJavaList(Long.class);
        if (requestObj == null) {
            return ResultMap.response(ResultCodeEnum.FAILED);
        }

        Map param = Maps.newHashMap();
        if (CollectionUtils.isNotEmpty(ids)) {
            param.put("materialIds", ids);
        }

        param.put("sidx", "id");
        param.put("order", "desc");
//        param.put("fields", "id, file_url");

        //查询列表数据
        List<ProductMaterialsVo> materialsList = productMaterialsService.queryMaterialsList(param);
        return ResultMap.response(ResultCodeEnum.SUCCESS, materialsList);
    }

    @PostMapping("save")
    public Object save(@LoginUser LoginUserVo userVo) {
        JSONObject requestObj = super.getJsonRequest();
        if (null != requestObj) {
            JSONArray array = requestObj.getJSONArray("fileList");
            String productName = requestObj.getString("productName");
            List<ProductMaterialsVo> fileList = JSONArray.parseArray(array.toJSONString(), ProductMaterialsVo.class);
            if (CollectionUtils.isNotEmpty(fileList)) {
                logger.info("fileList.size = " + fileList.size());
                fileList.stream().forEach(item -> {
                    item.setProductName(productName);
                });
                productMaterialsService.batchSave(fileList);
            }
            return ResultMap.response(ResultCodeEnum.SUCCESS);
        }
        return ResultMap.response(ResultCodeEnum.FAILED);
    }

    @PostMapping("delete")
    public Object delete(@LoginUser LoginUserVo loginUser) {
        JSONObject requestObj = super.getJsonRequest();
        if (null != requestObj) {
            JSONArray array = requestObj.getJSONArray("checkedIds");
            List<Long> checkedIds = JSONArray.parseArray(array.toJSONString(), Long.class);
            if (CollectionUtils.isNotEmpty(checkedIds)) {
                Long[] ids = Lists.newArrayList(checkedIds).toArray(new Long[0]);
                logger.info("ids.size = " + ids.length);
                productMaterialsService.batchDelete(ids);
            }
            return ResultMap.response(ResultCodeEnum.SUCCESS);
        }
        return ResultMap.response(ResultCodeEnum.FAILED);
    }

}
