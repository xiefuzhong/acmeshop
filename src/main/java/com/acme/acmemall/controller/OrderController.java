package com.acme.acmemall.controller;

import com.acme.acmemall.annotation.LoginUser;
import com.acme.acmemall.common.ResultMap;
import com.acme.acmemall.controller.reqeust.OrderSubmitRequest;
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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

/**
 * @description:订单相关
 * @author: ihpangzi
 * @time: 2023/2/17 15:53
 */
@Api(tags = "订单相关")
@RestController
@RequestMapping("/api/order")
public class OrderController extends ApiBase {

    @Autowired
    IOrderService orderService;

    @ApiOperation(value = "订单提交")
    @PostMapping("submit")
    public Object submit(@LoginUser LoginUserVo loginUser) {
        try {
            OrderSubmitRequest request = JSONObject.toJavaObject(getJsonRequest(), OrderSubmitRequest.class);
            request.check();
            ResultMap resultObj = orderService.submit(request, loginUser);
            if (null != resultObj) {
                logger.info("Order.submit()=="+JSONObject.toJSONString(resultObj));
                return resultObj;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return toResponsFail("提交失败");
    }

    /**
     * 获取订单列表
     */
    @ApiOperation(value = "获取订单列表")
    @RequestMapping("list")
    public Object list(@LoginUser LoginUserVo loginUser, Integer order_status,
                       @RequestParam(value = "page", defaultValue = "1") Integer page,
                       @RequestParam(value = "size", defaultValue = "10") Integer size) {
        Map params = Maps.newHashMap();
        params.put("user_id", loginUser.getUserId());
        params.put("page", page);
        params.put("limit", size);
        params.put("sidx", "id");
        params.put("order", "desc");
        if (order_status!=null){
            params.put("order_status", order_status);
        }

        //查询列表数据
        PageHelper.startPage(page, size);
        List<OrderVo> orderList = orderService.queryOrderList(params);
        PageUtils goodsData = new PageUtils(new PageInfo(orderList));
        return toResponsSuccess(goodsData);
    }
}
