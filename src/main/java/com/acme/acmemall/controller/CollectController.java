package com.acme.acmemall.controller;

import com.acme.acmemall.annotation.LoginUser;
import com.acme.acmemall.common.ResultMap;
import com.acme.acmemall.exception.ResultCodeEnum;
import com.acme.acmemall.model.CollectVo;
import com.acme.acmemall.model.LoginUserVo;
import com.acme.acmemall.service.ICollectService;
import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Maps;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @description:我的商品收藏夹
 * @author: ihpangzi
 * @time: 2023/3/10 10:28
 */
@RestController
@RequestMapping("/api/collect")
public class CollectController extends ApiBase {

    @Autowired
    ICollectService collectService;

    /**
     * 获取用户收藏
     */
    @ApiOperation(value = "获取用户收藏")
    @GetMapping("list")
    public Object list(@LoginUser LoginUserVo loginUser, Integer typeId) {

        Map param = new HashMap();
        param.put("user_id", loginUser.getUserId());
        param.put("type_id", typeId);
        List<CollectVo> collectEntities = collectService.queryList(param);
        return ResultMap.response(ResultCodeEnum.SUCCESS, collectEntities);
    }

    /**
     * 添加/取消收藏
     */
    @ApiOperation(value = "添加/取消收藏")
    @PostMapping("addordelete")
    public Object addordelete(@LoginUser LoginUserVo loginUser) {
        JSONObject jsonParam = getJsonRequest();
        Integer typeId = jsonParam.getInteger("typeId");
        Integer valueId = jsonParam.getInteger("valueId");

        Map param = Maps.newHashMap();
        param.put("user_id", loginUser.getUserId());
        param.put("type_id", typeId);
        param.put("value_id", valueId);
        List<CollectVo> collectEntities = collectService.queryList(param);
        String handleType = "add";
        CollectVo collectEntity;
        if (CollectionUtils.isEmpty(collectEntities)) {
            collectEntity = CollectVo.builder()
                    .add_time(System.currentTimeMillis() / 1000)
                    .is_attention(0)
                    .build();
            collectEntity.addCollect(loginUser.getUserId(), valueId, typeId);

        } else {
            handleType = "delete";
            collectEntity = collectEntities.stream().findFirst().get();
            collectEntity.deleteCollect(loginUser.getUserId(), valueId);
        }
        Integer collectRes = collectService.saveOrUpdate(collectEntity);

        if (collectRes > 0) {
            Map data = Maps.newHashMap();
            data.put("type", handleType);
            return ResultMap.ok(data);
        }
        return ResultMap.error("操作失败");
    }
}
