package com.acme.acmemall.controller;

import com.acme.acmemall.annotation.LoginUser;
import com.acme.acmemall.common.ResultMap;
import com.acme.acmemall.exception.ResultCodeEnum;
import com.acme.acmemall.model.CapitalFlowVo;
import com.acme.acmemall.model.LoginUserVo;
import com.acme.acmemall.service.IFinanceFowService;
import com.acme.acmemall.service.IUserService;
import com.acme.acmemall.utils.PageUtils;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.google.common.collect.Maps;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @description: 财务流水控制器
 * @author: ihpangzi
 * @time: 2024/4/25 17:48
 */
@RestController
@RequestMapping("/api/finance/flow")
public class FinanceFowController extends ApiBase {

    @Resource
    private IUserService userService;

    @Resource
    private IFinanceFowService financeFlowService;

    @GetMapping(value = "get-list")
    public Object getFinanceFlow(@LoginUser LoginUserVo loginUser,
                                 @RequestParam(value = "timeRange", defaultValue = "3") Integer timeRange,
                                 @RequestParam(value = "tradeType", defaultValue = "-1") Integer tradeType,
                                 @RequestParam(value = "page", defaultValue = "1") Integer page,
                                 @RequestParam(value = "size", defaultValue = "10") Integer size) {
        // TODO: 实现财务流水列表查询
        if (loginUser == null) {
            return toResponsFail("您未登录");
        }
        if (!userService.checkAdmin(loginUser.getUserId())) {
            return toResponsFail("您不是管理员");
        }
        Map<String, Object> params = Maps.newHashMap();
        if (timeRange != null && timeRange > 0) {
            Date daysAgo = new Date(System.currentTimeMillis() - timeRange * DAY_TIME);
            params.put("add_time", daysAgo.getTime() / 1000);
        }
        params.put("tradeType", tradeType);
        params.put("page", page);
        params.put("limit", size);
        params.put("order", "desc");
        PageHelper.startPage(page, size, true);
        List<CapitalFlowVo> flowList = financeFlowService.getFinanceFlowList(params);
        PageInfo pageInfo = new PageInfo(flowList);
        PageUtils pager = new PageUtils(pageInfo);
        return ResultMap.response(ResultCodeEnum.SUCCESS, pager);
    }
}
