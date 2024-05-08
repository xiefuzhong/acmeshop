package com.acme.acmemall.controller;

import com.acme.acmemall.annotation.LoginUser;
import com.acme.acmemall.common.ResultMap;
import com.acme.acmemall.controller.reqeust.OrderRefundRequest;
import com.acme.acmemall.controller.reqeust.OrderSubmitRequest;
import com.acme.acmemall.kuaidi100.response.QueryTrackResp;
import com.acme.acmemall.kuaidi100.service.KuaiDi100QueryService;
import com.acme.acmemall.kuainiao.ExpressService;
import com.acme.acmemall.model.*;
import com.acme.acmemall.service.*;
import com.acme.acmemall.utils.MapUtils;
import com.acme.acmemall.utils.PageUtils;
import com.acme.acmemall.utils.UserUtils;
import com.acme.acmemall.utils.wechat.WechatRefundApiResult;
import com.acme.acmemall.utils.wechat.WechatUtil;
import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.google.common.base.Throwables;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @description:订单相关
 * @author: ihpangzi
 * @time: 2023/2/17 15:53
 */
@Api(tags = "订单相关")
@RestController
@RequestMapping("/api/order")
public class OrderController extends ApiBase {

    @Resource
    IOrderService orderService;

    @Resource
    IOrderGoodsService orderGoodsService;

    @Resource
    KuaiDi100QueryService kuaiDi100QueryService;

    @Resource
    ExpressService expressService;

    @Resource
    IWeChatService wechatService;

    @Resource
    IOrderRefundService refundService;

    @Resource
    IUserService userService;

    @Resource
    ITokenService tokenService;

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
        params.put("sidx", "add_time");
        params.put("order", "desc");
        if (order_status != null) {
            params.put("order_status", order_status);
        }
        //查询列表数据
        PageHelper.startPage(page, size);
        List<OrderVo> orderList = orderService.queryOrderList(params);
        logger.info("Order.list=" + (CollectionUtils.isEmpty(orderList) ? 0 : orderList.size()));
        if (CollectionUtils.isNotEmpty(orderList)) {
            List<String> tmpOrderIds = orderList.stream().map(OrderVo::getId).collect(Collectors.toList());
            List<String> orderIds = tmpOrderIds.stream().distinct().collect(Collectors.toList());
            Map orderGoodsParam = Maps.newHashMap();
            orderGoodsParam.put("orderIds", orderIds);
            //订单的商品
            List<OrderGoodsVo> orderGoods = orderGoodsService.queryList(orderGoodsParam);
            Map<String, List<OrderGoodsVo>> orderGoodsMap = orderGoods.stream()
                    .collect(Collectors.groupingBy(OrderGoodsVo::getOrder_id));
            orderList.forEach(orderVo -> {
                orderVo.buildHandleOption(0);
                orderVo.fillItem(orderGoodsMap.get(orderVo.getId()));
            });
        }

        PageInfo pageInfo = new PageInfo<>(orderList);
        PageUtils goodsData = new PageUtils(pageInfo);

        return toResponsSuccess(goodsData);
    }

    /**
     * 获取订单详情
     */
    @ApiOperation(value = "获取订单详情")
    @GetMapping("detail")
    public Object detail(@LoginUser LoginUserVo userVo,
                         @RequestParam("orderId") String orderId) {
        if (userVo == null) {
            return ResultMap.badArgument("非有效用户操作");
        }

        Map resultObj = Maps.newHashMap();
        OrderVo orderInfo = orderService.findOrder(orderId);
        if (orderInfo == null) {
            return ResultMap.badArgument("查无此单:" + orderId);
        }
        Map orderGoodsParam = Maps.newHashMap();
        orderGoodsParam.put("orderIds", Lists.newArrayList(orderId));
        //订单的商品
        List<OrderGoodsVo> orderGoods = orderGoodsService.queryList(orderGoodsParam);
        // 售后记录
        OrderRefundVo refundVo = refundService.findByOrderId(orderId);
//        LoginUserVo loginUserVo = userService.queryByUserId(userVo.getUserId());
        //订单可操作的选择,删除，支付，收货，评论，退换货
        orderInfo.buildHandleOption(0);

        resultObj.put("orderInfo", orderInfo);
        resultObj.put("orderGoods", orderGoods);
        resultObj.put("handleOption", orderInfo.getHandleOption());
        resultObj.put("orderRefund", refundVo);
        if (!StringUtils.isEmpty(orderInfo.getShipping_code()) && !StringUtils.isEmpty(orderInfo.getShipping_no())) {
            resultObj.put("shippingList", null);
        }
        return ResultMap.ok(resultObj);
    }

    /**
     * 取消订单，恢复优惠券，更新订单状态
     */
    @ApiOperation(value = "取消订单")
//    @RequestMapping("cancel")
    public Object cancelOrder(@LoginUser LoginUserVo loginUserVo, String orderId) {
        try {
            OrderVo orderVo = orderService.findOrder(orderId);
            // 可取消-待付款-已付款、未发货
            if (!orderVo.canCancel()) {
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
                    if (StringUtils.equals("订单已全额退款", result.getErr_code_des())) {
                        orderVo.refund();
                    } else {
                        return ResultMap.error(400, "取消失败");
                    }

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
            orderService.updateOrder(orderVo);
            return ResultMap.ok("确认收货成功");
        } catch (Exception e) {
            logger.error(Throwables.getStackTraceAsString(e));
        }
        return ResultMap.error("提交失败");
    }

    @GetMapping("/delivery-track")
    public Object deliveryTrack(@LoginUser LoginUserVo loginUserVo, @RequestParam("orderId") String orderId) {
        OrderVo orderVo = orderService.findOrder(orderId);
        if (loginUserVo == null) {
            return ResultMap.error(400, "非有效用户操作");
        }
        if (orderVo == null) {
            return ResultMap.badArgument("查无此单,请确认订单信息");
        }
        Map<String, Object> paramMap = Maps.newHashMap();
        paramMap.put("com", orderVo.getShipping_code().toLowerCase(Locale.ROOT));
        paramMap.put("num", orderVo.getShipping_no());
        try {
            QueryTrackResp queryTrackResp = kuaiDi100QueryService.queryTrack(paramMap);
            return toResponsSuccess(queryTrackResp);
        } catch (Exception e) {
            ResultMap.error(e.getMessage());
        }
        return toResponsSuccess(null);
    }

    @GetMapping("/express-track")
    public Object expressTrack(@LoginUser LoginUserVo loginUserVo,
                               @RequestParam("orderId") String orderId,
                               @RequestParam("type") String type) {
        OrderVo orderVo = orderService.findOrder(orderId);
        if (loginUserVo == null) {
            return ResultMap.error(400, "非有效用户操作");
        }
        if (orderVo == null) {
            return ResultMap.badArgument("查无此单,请确认订单信息");
        }
        String shippingCode = orderVo.getShipping_code();
        String shippingNo = orderVo.getShipping_no();
        if (StringUtils.equalsIgnoreCase("refund", type)) {
            shippingCode = orderVo.getRefund_express();
            shippingNo = orderVo.getRefund_express_code();
        }
        logger.info("》》》物流助手>>>getPath");
        Map result = tokenService.getTokens(loginUserVo.getUserId());
        String accessToken = MapUtils.getString("token", result);
        String requestUrl = UserUtils.getWxExpressTrack(accessToken);
        Map params = Maps.newHashMap();
        params.put("openid", loginUserVo.getWeixin_openid());
        params.put("delivery_id", shippingCode);
        params.put("waybill_id", shippingNo);
        String res = wechatService.getPath(requestUrl, params);
        WaybillTrackVo track = JSONObject.toJavaObject(JSONObject.parseObject(res), WaybillTrackVo.class);
        return toResponsSuccess(track);
    }


    @PostMapping("/cancel")
    public Object cancel(@LoginUser LoginUserVo loginUser) {
        try {
            if (loginUser == null) {
                return ResultMap.error(400, "非有效用户操作");
            }
            JSONObject object = getJsonRequest();
            if (object == null) {
                return ResultMap.badArgument();
            }

            OrderVo orderVo = orderService.findOrder(object.getString("orderId"));
//            logger.info("用户取消订单 cancel before：" + orderVo.getOrderProcessText());
            if (orderVo == null) {
                return ResultMap.badArgument("查无此单,请确认订单信息");
            }
            if (!orderVo.getUser_id().equals(loginUser.getUserId())) {
                return ResultMap.badArgument("非法用户不能取消");
            }
            // 未付款，直接取消
            if (!orderVo.canCancel()) {
                return ResultMap.badArgument("当前状态下不能取消操作");
            }
            orderVo.cancle("用户取消");
//            logger.info("用户取消订单 cancel after：" + orderVo.getOrderProcessText());
            orderService.updateOrder(orderVo);
        } catch (Exception e) {
            String errMsg = Throwables.getStackTraceAsString(e);
            return ResultMap.error(errMsg);
        }
        return ResultMap.ok("取消成功");
    }

    @PostMapping("refund-apply")
    public Object refund(@LoginUser LoginUserVo loginUserVo) {
        try {
            JSONObject object = getJsonRequest();
            if (object == null) {
                return ResultMap.badArgument();
            }
            OrderRefundRequest request = JSONObject.toJavaObject(object, OrderRefundRequest.class);
            if (loginUserVo == null) {
                return ResultMap.error(400, "非有效用户操作");
            }
            request.setUserid(loginUserVo.getUserId());
            OrderVo orderVo = orderService.findOrder(request.getOrderId());
            if (orderVo == null) {
                return ResultMap.badArgument("查无此单,请确认订单信息");
            }
            if (!orderVo.getUser_id().equals(loginUserVo.getUserId())) {
                return ResultMap.badArgument("非法用户不能申请");
            }
            orderVo.refundRequest(request);
            orderService.updateOrder(orderVo);
            return ResultMap.ok("取消成功");
        } catch (Exception e) {
            return ResultMap.error(Throwables.getStackTraceAsString(e));
        }
    }

}
