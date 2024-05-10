package com.acme.acmemall.controller;

import com.acme.acmemall.annotation.LoginUser;
import com.acme.acmemall.common.ResultMap;
import com.acme.acmemall.exception.ResultCodeEnum;
import com.acme.acmemall.model.LoginUserVo;
import com.acme.acmemall.model.StatisticsVo;
import com.acme.acmemall.service.IUserService;
import com.acme.acmemall.service.IWorkbenchService;
import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Maps;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @description:商城工作台
 * @author: ihpangzi
 * @time: 2023/3/22 14:01
 */
@Api(tags = "商城工作台")
@RestController
@RequestMapping("/api/workbench")
public class WorkbenchController extends ApiBase {

    @Resource
    IWorkbenchService workbenchService;
    @Resource
    IUserService userService;

    @ApiOperation(value = "工作台订单统计")
    @RequestMapping("statistics")
    public Object statistics(@LoginUser LoginUserVo userVo) {
        if (userVo == null) {
            return ResultMap.response(ResultCodeEnum.USER_NOT_LOGIN);
        }
        if (!userService.checkAdmin(userVo.getUserId())) {
            return ResultMap.response(ResultCodeEnum.UNAUTHORIZED);
        }
        JSONObject object = super.getJsonRequest();
        Map<String, Object> params = Maps.newHashMap();
        // 商家订单统计
        params.put("merchant_id", object.getString("merchant_id"));
        List<StatisticsVo> statistics = workbenchService.statistics(params);
        Map<String, Integer> statisticsMap = statistics.stream().collect(Collectors.toMap(StatisticsVo::getType, StatisticsVo::getNum, (ke1, ke2) -> ke1));
        return ResultMap.response(ResultCodeEnum.SUCCESS, statisticsMap);
    }
}
