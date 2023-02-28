package com.acme.acmemall.controller;

import com.acme.acmemall.annotation.LoginUser;
import com.acme.acmemall.common.ResultMap;
import com.acme.acmemall.controller.reqeust.OrderSubmitRequest;
import com.acme.acmemall.model.LoginUserVo;
import com.acme.acmemall.model.OrderGoodsVo;
import com.acme.acmemall.model.OrderVo;
import com.acme.acmemall.service.IOrderGoodsService;
import com.acme.acmemall.service.IOrderService;
import com.acme.acmemall.utils.PageUtils;
import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.google.common.collect.Maps;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

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

    @Autowired
    IOrderGoodsService orderGoodsService;

    @ApiOperation(value = "订单提交")
    @PostMapping("submit")
    public Object submit(@LoginUser LoginUserVo loginUser) {
        try {
            OrderSubmitRequest request = JSONObject.toJavaObject(getJsonRequest(), OrderSubmitRequest.class);
            request.check();
            ResultMap resultObj = orderService.submit(request, loginUser);
            if (null != resultObj) {
                logger.info("Order.submit()==" + JSONObject.toJSONString(resultObj));
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
                       @RequestParam(value = "size", defaultValue = "3") Integer size) {
        Map params = Maps.newHashMap();
        params.put("user_id", loginUser.getUserId());
        params.put("page", page);
        params.put("limit", size);
        params.put("sidx", "id");
        params.put("order", "desc");
        if (order_status != null) {
            params.put("order_status", order_status);
        }
        //查询列表数据
        PageHelper.startPage(page, size);
        List<OrderVo> orderList = orderService.queryOrderList(params);
        logger.info("Order.list=" + (CollectionUtils.isEmpty(orderList) ? 0 : orderList.size()));
        PageInfo pageInfo = new PageInfo<>(orderList);
//        logger.info("pageinfo-->"+JSONObject.toJSONString(pageInfo));
        PageUtils goodsData = new PageUtils(pageInfo);
        return toResponsSuccess(goodsData);
    }

    /**
     * 获取订单详情
     */
    @ApiOperation(value = "获取订单详情")
    @GetMapping("detail")
    public Object detail(String orderId) {
        Map resultObj = Maps.newHashMap();
        //
        OrderVo orderInfo = orderService.findOrder(orderId);
        if (null == orderInfo) {
            return toResponsObject(400, "订单不存在", "");
        }
        Map orderGoodsParam = Maps.newHashMap();
        orderGoodsParam.put("order_id", orderId);
        //订单的商品
        List<OrderGoodsVo> orderGoods = orderGoodsService.queryList(orderGoodsParam);
        //订单最后支付时间

        //订单可操作的选择,删除，支付，收货，评论，退换货
        Map handleOption = orderInfo.getHandleOption();
        //
        resultObj.put("orderInfo", orderInfo);
        resultObj.put("orderGoods", orderGoods);
        resultObj.put("handleOption", handleOption);
        if (!StringUtils.isEmpty(orderInfo.getShipping_code()) && !StringUtils.isEmpty(orderInfo.getShipping_no())) {
            resultObj.put("shippingList", null);
        }
        return toResponsSuccess(resultObj);
    }

    /**
     * 取消订单，恢复优惠券，更新订单状态
     */
    @ApiOperation(value = "取消订单")
    @RequestMapping("cancel")
    public Object cancelOrder(@LoginUser LoginUserVo loginUserVo, String orderId) {
        try {
            OrderVo orderVo = orderService.findOrder(orderId);
            orderVo.cancle(orderVo, loginUserVo.getUserId());
            // 退款场景待@todo

            orderService.updateOrder(orderVo);
            return toResponsSuccess("取消成功");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return toResponsSuccess("提交失败");
    }


}
