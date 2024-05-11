package com.acme.acmemall.controller;

import com.acme.acmemall.annotation.LoginUser;
import com.acme.acmemall.cainiao.service.LogisticsCloudService;
import com.acme.acmemall.cainiao.vo.TraceVo;
import com.acme.acmemall.common.ResultMap;
import com.acme.acmemall.model.LoginUserVo;
import com.alibaba.fastjson.JSONObject;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * @description:物流信息
 * @author: ihpangzi
 * @time: 2024/1/23 13:55
 */
@RestController
@RequestMapping("/api/logistics")
public class LogisticsController extends ApiBase {

    @Resource
    LogisticsCloudService logisticsCloudService;

    @PostMapping("/traces")
    public Object queryExpressRoutes(@LoginUser LoginUserVo loginUser) {
        if (loginUser == null) {
            return ResultMap.error("请先登录");
        }
        JSONObject jsonObject = getJsonRequest();
        if (jsonObject == null) {
            return ResultMap.error("参数错误");
        }
        String expressNo = jsonObject.getString("expressNo");
        String result = logisticsCloudService.queryExpressRoutes(expressNo);
        JSONObject object = JSONObject.parseObject(result);
        TraceVo traceVo = JSONObject.toJavaObject(object, TraceVo.class);
        return toResponsSuccess(traceVo);
    }

    @PostMapping("/intercept")
    public Object interceptPackage(@LoginUser LoginUserVo loginUser) {
        if (loginUser == null) {
            return ResultMap.error("请先登录");
        }
        JSONObject jsonObject = getJsonRequest();
        if (jsonObject == null) {
            return ResultMap.error("参数错误");
        }
        String expressNo = jsonObject.getString("expressNo");
        String cpCode = jsonObject.getString("cpCode");
        String result = logisticsCloudService.interceptPackage(expressNo, cpCode);
        return toResponsSuccess(result);
    }
}
