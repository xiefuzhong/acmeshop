package com.acme.acmemall.controller;

import com.acme.acmemall.annotation.IgnoreAuth;
import com.acme.acmemall.annotation.LoginUser;
import com.acme.acmemall.common.ResultMap;
import com.acme.acmemall.model.LoginUserVo;
import com.acme.acmemall.model.OrderGoodsVo;
import com.acme.acmemall.model.OrderVo;
import com.acme.acmemall.service.IOrderGoodsService;
import com.acme.acmemall.service.IOrderService;
import com.acme.acmemall.utils.MapUtils;
import com.acme.acmemall.utils.ResourceUtil;
import com.acme.acmemall.utils.XmlUtil;
import com.acme.acmemall.utils.cache.J2EcacheUtil;
import com.acme.acmemall.utils.wechat.NonceUtil;
import com.acme.acmemall.utils.wechat.WechatRefundApiResult;
import com.acme.acmemall.utils.wechat.WechatUtil;
import com.google.common.base.Throwables;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.math.BigDecimal;
import java.text.MessageFormat;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

/**
 * @description:支付服务(微信)
 * @author: ihpangzi
 * @time: 2023/2/24 16:51
 */
@Api(tags = "支付服务")
@RestController
@RequestMapping("/api/pay")
public class PayController extends ApiBase {

    @Resource
    IOrderService orderService;

    @Resource
    IOrderGoodsService orderGoodsService;

    private static final String SUCCESS = "SUCCESS";
    private static final String OK = "OK";

    public static String setXml(String return_code, String return_msg) {
        return "<xml><return_code><![CDATA[" + return_code + "]]></return_code><return_msg><![CDATA[" + return_msg + "]]></return_msg></xml>";
    }

    /**
     * 获取待支付订单的请求参数
     */
    @ApiOperation(value = "获取待支付订单的请求参数")
    @GetMapping("prepay")
    public Object payPrepay(@LoginUser LoginUserVo loginUser, String orderId) {
        OrderVo orderVo = orderService.findOrder(orderId);
        if (orderVo == null) {
            return ResultMap.error(400, "订单不存在");
        }
        if (!orderVo.checkOwner(loginUser.getUserId())) {
            return ResultMap.error(400, "用户不存在");
        }
        if (!orderVo.canPay()) {
            return ResultMap.error(400, "当前订单已支付,请不要重复操作");
        }

        Map orderGoodsParam = Maps.newHashMap();
        orderGoodsParam.put("orderIds", Lists.newArrayList(orderId));
        //订单的商品
        List<OrderGoodsVo> orderGoods = orderGoodsService.queryList(orderGoodsParam);
        orderVo.fillItem(orderGoods);
        //https://pay.weixin.qq.com/wiki/doc/api/wxa/wxa_api.php?chapter=7_7&index=3
        Map<Object, Object> resultObj = new TreeMap();

        try {
            Map<Object, Object> parame = new TreeMap<Object, Object>();
            parame.put("appid", ResourceUtil.getConfigByName("wx.appId"));
            // 商家账号。
            parame.put("mch_id", ResourceUtil.getConfigByName("wx.mchId"));
            // 随机字符串
            parame.put("nonce_str", NonceUtil.createNonce(18));
            // 商户订单编号
            parame.put("out_trade_no", orderId);
            // 商品描述
            parame.put("body", orderVo.getPayBody_title());
            //支付金额
            parame.put("total_fee", orderVo.getActual_price().multiply(new BigDecimal(100)).intValue());
            logger.info("***************" + parame.get("total_fee") + "***************");
            //parame.put("total_fee", 1);
            // 回调地址 @todo
            parame.put("notify_url", ResourceUtil.getConfigByName("wx.notifyUrl"));
            // 交易类型APP
            parame.put("trade_type", ResourceUtil.getConfigByName("wx.tradeType"));
            parame.put("spbill_create_ip", getPayClientIp());
            parame.put("openid", loginUser.getWeixin_openid());
            String sign = WechatUtil.arraySign(parame, ResourceUtil.getConfigByName("wx.paySignKey"));
            logger.info("[" + sign + "]");
            // 数字签证
            parame.put("sign", sign);

            String xml = MapUtils.convertMap2Xml(parame);
            logger.info("***************xml=" + xml + "***************");
            Map<String, Object> resultUn = XmlUtil.xmlStrToMap(WechatUtil.requestOnce(ResourceUtil.getConfigByName("wx.uniformorder"), xml));
            logger.info("================resultUn=" + resultUn + "================");

            // 响应报文
            String return_code = MapUtils.getString("return_code", resultUn);
            String return_msg = MapUtils.getString("return_msg", resultUn);
            //
            if (return_code.equalsIgnoreCase("FAIL")) {
                return toResponsFail("支付失败," + return_msg);
            } else if (return_code.equalsIgnoreCase("SUCCESS")) {
                // 返回数据
                String result_code = MapUtils.getString("result_code", resultUn);
                String err_code_des = MapUtils.getString("err_code_des", resultUn);
                if (result_code.equalsIgnoreCase("FAIL")) {
                    String err_code = MapUtils.getString("err_code", resultUn);
                    if ("ORDERPAID".equalsIgnoreCase(err_code)) {
                        String prepay_id = MapUtils.getString("prepay_id", resultUn);
                        // 该订单已支付
                        OrderVo newOrder = OrderVo.builder().id(orderVo.getId()).pay_id(prepay_id).build();
                        newOrder.paid();
                        orderService.updateStatus(newOrder);
                    }
                    return toResponsFail("支付失败," + err_code_des);
                } else if (result_code.equalsIgnoreCase("SUCCESS")) {
                    String prepay_id = MapUtils.getString("prepay_id", resultUn);
                    // 先生成paySign 参考https://pay.weixin.qq.com/wiki/doc/api/wxa/wxa_api.php?chapter=7_7&index=5
                    resultObj.put("appId", ResourceUtil.getConfigByName("wx.appId"));
                    //resultObj.put("timeStamp", DateUtils.timeToStr(System.currentTimeMillis() / 1000, DateUtils.DATE_TIME_PATTERN));
                    resultObj.put("timeStamp", System.currentTimeMillis() / 1000 + "");
                    resultObj.put("nonceStr", NonceUtil.createNonce(32));
                    resultObj.put("package", "prepay_id=" + prepay_id);
                    resultObj.put("signType", "MD5");
                    String paySign = WechatUtil.arraySign(resultObj, ResourceUtil.getConfigByName("wx.paySignKey"));
                    resultObj.put("paySign", paySign);

                    OrderVo newOrder = OrderVo.builder().id(orderVo.getId()).pay_id(prepay_id).build();
                    newOrder.prePay(newOrder);
                    orderService.updateStatus(newOrder);

                    //redis设置订单状态@todo
//                    RedisUtils.set(allOrderId.toString(), "51", 60*60*24);
                    J2EcacheUtil.getInstance().put(J2EcacheUtil.SHOP_CACHE_NAME, orderId, "51");
                    return toResponsObject(0, "微信统一订单下单成功", resultObj);
                }
            }
        } catch (Exception e) {
            logger.error(Throwables.getStackTraceAsString(e));
            return toResponsFail("下单失败,error=" + e.getMessage());
        }
        return toResponsFail("下单失败");
    }

    /**
     * 微信订单回调接口
     *
     */
    @ApiOperation(value = "微信订单回调接口")
    @RequestMapping(value = "/notify", method = RequestMethod.POST, produces = "text/html;charset=UTF-8")
    @IgnoreAuth
    @ResponseBody
    public void notify(HttpServletRequest request, HttpServletResponse response) {
        try {
            request.setCharacterEncoding("UTF-8");
            response.setCharacterEncoding("UTF-8");
            response.setContentType("text/html;charset=UTF-8");
            response.setHeader("Access-Control-Allow-Origin", "*");
            InputStream in = request.getInputStream();
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            byte[] buffer = new byte[1024];
            int len = 0;
            while ((len = in.read(buffer)) != -1) {
                out.write(buffer, 0, len);
            }
            out.close();
            in.close();
            //xml数据
            String reponseXml = new String(out.toByteArray(), "utf-8");

            WechatRefundApiResult result = (WechatRefundApiResult) XmlUtil.xmlStrToBean(reponseXml, WechatRefundApiResult.class);

            //处理订单的redis状态
            String value = J2EcacheUtil.getInstance().get(J2EcacheUtil.SHOP_CACHE_NAME, result.getOut_trade_no()).toString();
            if (value != null && "51".equals(value)) {
                J2EcacheUtil.getInstance().delete(J2EcacheUtil.SHOP_CACHE_NAME, result.getOut_trade_no());
            } else {
                //查询支付已结操作过
                response.getWriter().write(setXml(SUCCESS, OK));
                return;
            }
            OrderVo orderVo = orderService.findOrder(result.getOut_trade_no());
            if (!orderVo.getPay_status().equals(1)) {
                response.getWriter().write(setXml(SUCCESS, OK));
                return;
            }

            String result_code = result.getResult_code();
            if (result_code.equalsIgnoreCase("FAIL")) {
                //订单编号
                String out_trade_no = result.getOut_trade_no();
                logger.error("订单" + out_trade_no + "支付失败");
                response.getWriter().write(setXml(SUCCESS, OK));
            } else if (result_code.equalsIgnoreCase(SUCCESS)) {
                Map<Object, Object> retMap = XmlUtil.xmlStrToTreeMap(reponseXml);
                String sign = WechatUtil.arraySign(retMap, ResourceUtil.getConfigByName("wx.paySignKey"));
                if (!sign.equals(result.getSign())) {//判断签名
                    return;
                }

                // 更改订单状态
                // 业务处理
                OrderVo orderInfo = orderService.findOrder(result.getOut_trade_no());
                orderInfo.paid();

                orderService.updateStatus(orderInfo);

//                Map<String, Object> map = new HashMap<String, Object>();
//                map.put("all_order_id", result.getOut_trade_no());
//                List<OrderVo> lists = orderService.queryList(map);
//                OrderVo vo = null;
//                for(OrderVo v : lists) {
//                    vo = v;
//                    try {
//                        //调用分销接口(现在支付成功就分润，后期要改造变成收货后，或者变成不可以体现的分润)
//                        fx(new Long(vo.getPromoter_id()), vo.getBrokerage(), vo.getOrder_price(), vo.getId(), vo.getMerchant_id());
//                    }catch(Exception e) {
//                        System.out.println("================分销错误开始================");
//                        e.printStackTrace();
//                        System.out.println("================分销错误结束================");
//                    }
//                }


                response.getWriter().write(setXml(SUCCESS, OK));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     * 微信查询订单状态
     */
    @ApiOperation(value = "查询订单状态")
    @GetMapping("query")
    public Object queryPayStatus(@LoginUser LoginUserVo loginUser, String orderId) {
        OrderVo orderVo = orderService.findOrder(orderId);
        if (orderVo == null) {
            return ResultMap.error(400, "订单不存在");
        }
        if (!orderVo.checkOwner(loginUser.getUserId())) {
            return ResultMap.error(400, "用户不存在");
        }

        // 已付款
        if (orderVo.paidCheck()) {
            return ResultMap.ok("支付成");
        }
        Map<Object, Object> query = Maps.newHashMap();
        query.put("appid", ResourceUtil.getConfigByName("wx.appId"));
        // 商家账号。
        query.put("mch_id", ResourceUtil.getConfigByName("wx.mchId"));
        String randomStr = NonceUtil.createNonce(18);
        // 随机字符串
        query.put("nonce_str", randomStr);
        // 商户订单编号
        query.put("out_trade_no", orderId);

        String sign = WechatUtil.arraySign(query, ResourceUtil.getConfigByName("wx.paySignKey"));
        // 数字签证
        query.put("sign", sign);
        String xml = MapUtils.convertMap2Xml(query);
        logger.info("xml:" + xml);
        Map<String, Object> queryResult = null;
        try {
            String result = WechatUtil.requestOnce(ResourceUtil.getConfigByName("wx.orderquery"), xml);
            queryResult = XmlUtil.xmlStrToMap(result);
        } catch (Exception e) {
            e.printStackTrace();
            return ResultMap.error("查询失败,error=" + e.getMessage());
        }
        // 响应报文
        String resCode = MapUtils.getString("return_code", queryResult);
        String resMsg = MapUtils.getString("return_msg", queryResult);
        if (!StringUtils.equalsIgnoreCase(SUCCESS, resCode)) {
            String msg = MessageFormat.format("查询失败,error_code={0},msg={1}", resCode, resMsg);
            return ResultMap.error(msg);
        }
        // @todo 支付订单查询成功后，可以通过事件触发做订单的状态更新以及后续的事情实现解耦。
        String tradeState = MapUtils.getString("trade_state", queryResult);
        if (StringUtils.equalsIgnoreCase(SUCCESS, tradeState)) {
            // 支付成功,业务处理
            orderVo.paid();
            orderService.updateStatus(orderVo);
            return ResultMap.ok("支付成功");
        } else if (StringUtils.equalsIgnoreCase("USERPAYING", tradeState)) {
            // 重新查询 支付中
            String key = "queryRepeatNum" + orderId;
            Integer num = (Integer) J2EcacheUtil.getInstance().get(J2EcacheUtil.SHOP_CACHE_NAME, key);
            if (num == null) {
                J2EcacheUtil.getInstance().put(J2EcacheUtil.SHOP_CACHE_NAME, key, 1);
                this.queryPayStatus(loginUser, orderId);
            } else if (num <= 3) {
                J2EcacheUtil.getInstance().delete(J2EcacheUtil.SHOP_CACHE_NAME, key);
                this.queryPayStatus(loginUser, orderId);
            } else {
                return ResultMap.error("查询失败,error=" + tradeState);
            }
            orderVo.resetPayStatus();
            orderService.updateStatus(orderVo);
        } else {
            orderVo.resetPayStatus();
            orderService.updateStatus(orderVo);
            return ResultMap.error("查询失败,error=" + tradeState);
        }

        return ResultMap.error("查询失败，未知错误");
    }

    /**
     * 订单退款请求
     *
     * @param loginUserVo
     * @param orderId
     * @return
     */
    @ApiOperation(value = "订单退款请求")
    @PostMapping("refund")
    public Object refund(@LoginUser LoginUserVo loginUserVo, String orderId) {
        OrderVo orderVo = orderService.findOrder(orderId);
        if (orderVo == null) {
            return ResultMap.error(400, "订单不存在");
        }
        if (orderVo.checkOwner(loginUserVo.getUserId())) {
            return ResultMap.error(400, "非有效用户操作");
        }
        if (orderVo.refundStatus()) {
            return ResultMap.error(400, "订单已退款");
        }
        if (!orderVo.paidCheck()) {
            return ResultMap.error(400, "订单未付款，不能退款");
        }
        WechatRefundApiResult result = WechatUtil.wxRefund(orderId, orderVo.getActual_price().doubleValue(), orderVo.getActual_price().doubleValue());
        if (StringUtils.equalsIgnoreCase(SUCCESS, result.getResult_code())) {
            orderVo.refund();
            orderService.updateOrder(orderVo);
            return ResultMap.ok("操作成功，请查看账户");
        }
        return ResultMap.error("操作失败，请重试");
    }

}
