package com.acme.acmemall.controller;

import com.acme.acmemall.annotation.LoginUser;
import com.acme.acmemall.common.ResultMap;
import com.acme.acmemall.controller.reqeust.OrderShippedRequest;
import com.acme.acmemall.exception.ResultCodeEnum;
import com.acme.acmemall.model.LoginUserVo;
import com.acme.acmemall.model.OrderVo;
import com.acme.acmemall.service.IOrderService;
import com.acme.acmemall.utils.PageUtils;
import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.google.common.collect.Maps;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

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

    IOrderService orderService;

    public ShopOrderController(IOrderService orderService) {
        this.orderService = orderService;
    }

    @ApiOperation(value = "商户订单列表")
    @RequestMapping("list")
    public Object listMerchantOrder(
            @LoginUser LoginUserVo loginUser,
            @RequestParam(value = "timeType", defaultValue = "order") String timeType,
            @RequestParam(value = "order_status", defaultValue = "-1") Integer order_status,
            @RequestParam(value = "merchant_id", defaultValue = "0") Long merchant_id,
            @RequestParam(value = "timeRange", defaultValue = "1") Integer timeRange,
            @RequestParam(value = "page", defaultValue = "1") Integer page,
            @RequestParam(value = "size", defaultValue = "10") Integer size) {
        Map params = Maps.newHashMap();
        params.put("user_id", loginUser.getUserId());
        params.put("page", page);
        params.put("limit", size);
        if (order_status > 0) {
            params.put("sidx", "o.order_status,o.add_time"); // 待付款，下单时间
        }
        params.put("sidx", "o.id,o.order_status,o.add_time"); // 待付款，下单时间
        params.put("order", "desc");
        params.put("merchant_id", merchant_id); // 商户ID
        if (order_status != null) {
            params.put("order_status", order_status);
        }
        params.put("timeRange", timeRange);
        params.put("timeType", timeType);
        //查询列表数据
        PageHelper.startPage(page, size);
        List<OrderVo> orders = orderService.queryOrderList(params);
        PageInfo pageInfo = new PageInfo<>(orders);
        PageUtils ordersPage = new PageUtils(pageInfo);
        return ResultMap.response(ResultCodeEnum.SUCCESS, ordersPage);
    }

    @ApiOperation(value = "商户备注订单")
    @RequestMapping("remark")
    public Object remarkOrder(@LoginUser LoginUserVo loginUserVo,
                              @RequestParam(value = "orderId", defaultValue = "0") String orderId,
                              @RequestParam(value = "handleType") String type,
                              @RequestParam(value = "remarkText") String remarkText) {
        OrderVo orderVo = orderService.findOrder(orderId);
        OrderVo updateOrderVo = OrderVo.builder().handleType(type).merRemark(remarkText).build();
        orderVo.handle(updateOrderVo);
        orderService.handleOrderByMer(orderVo);
        return ResultMap.ok();
    }

    @PostMapping("/update")
    public Object updateOrder(@LoginUser LoginUserVo userVo) {
        JSONObject jsonRequest = super.getJsonRequest();
        if (jsonRequest == null) {
            return ResultMap.badArgument();
        }
        logger.info("updateOrder request:" + jsonRequest);
        OrderShippedRequest shippedRequest = JSONObject.toJavaObject(jsonRequest, OrderShippedRequest.class);

        String orderId = shippedRequest.getOrderId();
        OrderVo orderVo = orderService.findOrder(orderId);
        if (orderVo == null) {
            return ResultMap.badArgument("查无此单:" + orderId);
        }
        orderVo.shipped(shippedRequest);
        orderService.handleOrderByMer(orderVo);
        return toResponsSuccess(orderVo);
    }
}
