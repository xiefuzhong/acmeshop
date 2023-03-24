package com.acme.acmemall.controller;

import com.acme.acmemall.annotation.LoginUser;
import com.acme.acmemall.common.ResultMap;
import com.acme.acmemall.exception.ResultCodeEnum;
import com.acme.acmemall.model.LoginUserVo;
import com.acme.acmemall.model.OrderVo;
import com.acme.acmemall.service.IOrderService;
import com.acme.acmemall.utils.PageUtils;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * @description:小店订单相关
 * @author: ihpangzi
 * @time: 2023/3/23 16:23
 */
@Api(tags = "订单相关")
@RestController
@RequestMapping("/api/mer-order")
public class ShopOrderController extends ApiBase {

    @Resource
    IOrderService orderService;

    @ApiOperation(value = "商户订单列表")
    @RequestMapping("list")
    public Object listMerchantOrder(
            @LoginUser LoginUserVo loginUser,
            Integer order_status,
            Long member_id,
            Integer timeRange,
            @RequestParam(value = "page", defaultValue = "1") Integer page,
            @RequestParam(value = "size", defaultValue = "10") Integer size) {
        Map params = Maps.newHashMap();
        params.put("user_id", loginUser.getUserId());
        params.put("page", page);
        params.put("limit", size);
        params.put("sidx", "id");
        params.put("order", "desc");
        params.put("member_id", member_id); // 商户ID
        if (order_status != null) {
            List<Integer> statusList = Lists.newArrayList(order_status);
            if (order_status == 301) {
                // 已发货 300-订单已发货， 301-用户确认收货
                statusList = Lists.newArrayList(300, 301);
            }
            params.put("statusList", statusList);
        }
        params.put("timeRange", timeRange);
        //查询列表数据
        PageHelper.startPage(page, size);
        List<OrderVo> orders = orderService.queryOrderList(params);
        PageInfo pageInfo = new PageInfo<>(orders);
        PageUtils ordersPage = new PageUtils(pageInfo);
        return ResultMap.response(ResultCodeEnum.SUCCESS, ordersPage);
    }
}
