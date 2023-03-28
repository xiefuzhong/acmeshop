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
import com.acme.acmemall.utils.wechat.WechatRefundApiResult;
import com.acme.acmemall.utils.wechat.WechatUtil;
import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.google.common.collect.Maps;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
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

    IOrderService orderService;

    IOrderGoodsService orderGoodsService;

    public OrderController(IOrderService orderService, IOrderGoodsService orderGoodsService) {
        this.orderService = orderService;
        this.orderGoodsService = orderGoodsService;
    }

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
        return ResultMap.ok(resultObj);
    }

    /**
     * 取消订单，恢复优惠券，更新订单状态
     */
    @ApiOperation(value = "取消订单")
    @RequestMapping("cancel")
    public Object cancelOrder(@LoginUser LoginUserVo loginUserVo, String orderId) {
        try {
            OrderVo orderVo = orderService.findOrder(orderId);
            // 可取消-待付款-已付款、未发货
            if (!orderVo.validCancle()) {
                return ResultMap.error(400, "当前状态下不能取消操作");
            }
            // 需要退款
            if (orderVo.getPay_status() == 2) {
                WechatRefundApiResult result = WechatUtil.wxRefund(orderId,
                        orderVo.getActual_price().doubleValue(),
                        orderVo.getActual_price().doubleValue());
                if (StringUtils.equalsIgnoreCase("SUCCESS", result.getResult_code())) {
                    orderVo.refund();
                    // todo:优惠券回退。如有分润，分润退还
                } else {
                    return ResultMap.error(400, "取消失败");
                }
            }
            orderVo.cancle(orderVo, loginUserVo.getUserId());
            orderService.updateOrder(orderVo);
            return ResultMap.ok("取消成功");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ResultMap.error("取消失败");
    }

    /**
     * 删除已取消的订单
     */
    @ApiOperation(value = "删除已取消的订单")
    @RequestMapping("delete")
    public Object deleteOrder(@LoginUser LoginUserVo loginUserVo, String orderId) {
        try {
            OrderVo orderVo = orderService.findOrder(orderId);
            orderVo.delete(orderVo, loginUserVo.getUserId());
            orderService.updateOrder(orderVo);
            return ResultMap.ok("操作成功");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ResultMap.error("操作失败");
    }

    /**
     * 确认收货
     */
    @ApiOperation(value = "确认收货")
    @RequestMapping("confirmOrder")
    public Object confirmOrder(@LoginUser LoginUserVo loginUserVo, String orderId) {
        try {
            OrderVo orderVo = orderService.findOrder(orderId);
            orderVo.confirm();
            orderService.updateStatus(orderVo);
            return ResultMap.ok("确认收货成功");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ResultMap.error("提交失败");
    }

}
