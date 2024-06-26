package com.acme.acmemall.controller;

import com.acme.acmemall.annotation.LoginUser;
import com.acme.acmemall.common.ResultMap;
import com.acme.acmemall.controller.reqeust.OrderShippedRequest;
import com.acme.acmemall.exception.Assert;
import com.acme.acmemall.exception.ResultCodeEnum;
import com.acme.acmemall.model.LoginUserVo;
import com.acme.acmemall.model.OrderRefundVo;
import com.acme.acmemall.model.OrderVo;
import com.acme.acmemall.model.enums.ShipStatusEnum;
import com.acme.acmemall.service.IOrderRefundService;
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
import org.apache.commons.lang.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
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

    @Resource
    IOrderRefundService refundService;

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
        params.put("sidx", "o.add_time"); // 待付款，下单时间
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
        orders.forEach(orderVo -> orderVo.buildHandleOption(merchant_id));
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
        OrderVo updateOrderVo = OrderVo.builder()
                .id(orderId)
                .handleType(type)
                .merRemark(remarkText)
                .build();
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
        OrderShippedRequest request = JSONObject.toJavaObject(jsonRequest, OrderShippedRequest.class);

        String orderId = request.getOrderId();
        OrderVo orderVo = orderService.findOrder(orderId);
        if (orderVo == null) {
            return ResultMap.badArgument("查无此单:" + orderId);
        }
        if (StringUtils.equalsIgnoreCase("shipping", request.getHandle())) {
            orderVo.shipped(request);
        } else if (StringUtils.equalsIgnoreCase("cancel", request.getHandle())) {
            orderVo.cancle("商家取消");
        } else if (StringUtils.equalsIgnoreCase("receipt", request.getHandle())) {
            OrderRefundVo refundVo = refundService.findByOrderId(orderId);
            orderVo.merReceipt(refundVo);
        } else if (StringUtils.equalsIgnoreCase("print", request.getHandle())) {
            // 打印面单
            orderVo.print(request);
        }
        logger.info("mer.updateOrder ===>" + orderVo);
        orderService.handleOrderByMer(orderVo);
        // 下单
        return toResponsSuccess(orderVo);
    }

    @RequestMapping("refund-audit")
    public Object refundAudit(@LoginUser LoginUserVo userVo,
                              @RequestParam("orderId") String orderId) {
        Assert.isBlank(orderId, "订单号不能为空");
        Assert.isNull(userVo, "非有效用户操作");
        OrderVo orderVo = orderService.findOrder(orderId);
        Assert.isNull(orderVo, "查无此单:" + orderId);

        // type = 1 仅退款流程(已付款未发货) 2，退货退款流程(已付款已发货)
        if (orderVo.refundStatus()) {
            return ResultMap.error(400, "订单已退款");
        }
        if (!orderVo.paidCheck()) {
            return ResultMap.error(400, "订单未付款，不能退款");
        }
        OrderRefundVo refundVo = refundService.findByOrderId(orderId);
        Assert.isNull(refundVo, "没有查到对应的售后单:" + orderId);

        // 仅退款
        if (refundVo.getRefund_type() == 1 && orderVo.getPay_status() == 2) {
            // 退款
            WechatRefundApiResult result = WechatUtil.wxRefund(orderId, orderVo.getActual_price().doubleValue(), orderVo.getActual_price().doubleValue());
            if (StringUtils.equalsIgnoreCase("SUCCESS", result.getResult_code())) {
                refundVo.audit();
                orderVo.unshippedRefund(refundVo);
                orderService.updateOrder(orderVo);
            } else {
                return ResultMap.badArgument(result.getErr_code_des());
            }
        } else if (refundVo.getRefund_type() == 2 && orderVo.getPay_status() == 2) {
            // 退货退款
            if (orderVo.getShipping_status() != ShipStatusEnum.SHIP_RETURN.getCode()) {
                return ResultMap.error("商品退回未被确认");
            }
            WechatRefundApiResult result = WechatUtil.wxRefund(orderId, orderVo.getActual_price().doubleValue(), orderVo.getActual_price().doubleValue());
            if (StringUtils.equalsIgnoreCase("SUCCESS", result.getResult_code())) {
                refundVo.audit();
                orderVo.unshippedRefund(refundVo);
                orderService.updateOrder(orderVo);
            } else {
                return ResultMap.badArgument(result.getErr_code_des());
            }

        }
        return ResultMap.ok();
    }
}
