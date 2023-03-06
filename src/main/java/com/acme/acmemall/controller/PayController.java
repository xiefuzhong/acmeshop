package com.acme.acmemall.controller;

import com.acme.acmemall.annotation.IgnoreAuth;
import com.acme.acmemall.annotation.LoginUser;
import com.acme.acmemall.common.ResultMap;
import com.acme.acmemall.model.LoginUserVo;
import com.acme.acmemall.model.OrderVo;
import com.acme.acmemall.service.IOrderService;
import com.acme.acmemall.utils.CharUtil;
import com.acme.acmemall.utils.MapUtils;
import com.acme.acmemall.utils.ResourceUtil;
import com.acme.acmemall.utils.XmlUtil;
import com.acme.acmemall.utils.wechat.WechatRefundApiResult;
import com.acme.acmemall.utils.wechat.WechatUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.math.BigDecimal;
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

    @Autowired
    IOrderService orderService;

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
        String body = orderVo.getPayBody_title();
        String nonceStr = CharUtil.getRandomString(32);

        //https://pay.weixin.qq.com/wiki/doc/api/wxa/wxa_api.php?chapter=7_7&index=3
        Map<Object, Object> resultObj = new TreeMap();

        try {
            Map<Object, Object> parame = new TreeMap<Object, Object>();
            parame.put("appid", ResourceUtil.getConfigByName("wx.appId"));
            // 商家账号。
            parame.put("mch_id", ResourceUtil.getConfigByName("wx.mchId"));
            String randomStr = CharUtil.getRandomNum(18).toUpperCase();
            // 随机字符串
            parame.put("nonce_str", randomStr);
            // 商户订单编号
            parame.put("out_trade_no", orderId);
            // 商品描述
            parame.put("body", body);
            //支付金额
            parame.put("total_fee", orderVo.getActual_price().multiply(new BigDecimal(100)).intValue());
            logger.info("***************" + parame.get("total_fee") + "***************");
            //System.out.println(orderInfo.getActual_price().multiply(new BigDecimal(100)).intValue() + "***************");
            //parame.put("total_fee", 1);
            // 回调地址 @todo
            parame.put("notify_url", ResourceUtil.getConfigByName("wx.notifyUrl"));
            // 交易类型APP
            parame.put("trade_type", ResourceUtil.getConfigByName("wx.tradeType"));
            parame.put("spbill_create_ip", getClientIp());
            parame.put("openid", loginUser.getWeixin_openid());
            String sign = WechatUtil.arraySign(parame, ResourceUtil.getConfigByName("wx.paySignKey"));
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
                    return toResponsFail("支付失败," + err_code_des);
                } else if (result_code.equalsIgnoreCase("SUCCESS")) {
                    String prepay_id = MapUtils.getString("prepay_id", resultUn);
                    // 先生成paySign 参考https://pay.weixin.qq.com/wiki/doc/api/wxa/wxa_api.php?chapter=7_7&index=5
                    resultObj.put("appId", ResourceUtil.getConfigByName("wx.appId"));
                    //resultObj.put("timeStamp", DateUtils.timeToStr(System.currentTimeMillis() / 1000, DateUtils.DATE_TIME_PATTERN));
                    resultObj.put("timeStamp", System.currentTimeMillis() / 1000 + "");
                    resultObj.put("nonceStr", nonceStr);
                    resultObj.put("package", "prepay_id=" + prepay_id);
                    resultObj.put("signType", "MD5");
                    String paySign = WechatUtil.arraySign(resultObj, ResourceUtil.getConfigByName("wx.paySignKey"));
                    resultObj.put("paySign", paySign);

                    OrderVo newOrder = OrderVo.builder().all_order_id(orderVo.getAll_order_id()).pay_id(prepay_id).build();
                    newOrder.prePay(newOrder);
                    orderService.updateStatus(newOrder);

                    //redis设置订单状态@todo
//                    RedisUtils.set(allOrderId.toString(), "51", 60*60*24);
                    return toResponsObject(0, "微信统一订单下单成功", resultObj);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            return toResponsFail("下单失败,error=" + e.getMessage());
        }
        return toResponsFail("下单失败");
    }

    /**
     * 微信订单回调接口
     *
     * @return
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
//            String value = RedisUtils.get(result.getOut_trade_no().toString());
//            if(value != null && "51".equals(value)) {
//                RedisUtils.del(result.getOut_trade_no().toString());
//            }else {
//                //查询支付已结操作过
//                response.getWriter().write(setXml("SUCCESS", "OK"));
//                return;
//            }
            OrderVo orderVo = orderService.findOrder(result.getOut_trade_no());
            if (!orderVo.getPay_status().equals(1)){
                response.getWriter().write(setXml("SUCCESS", "OK"));
                return;
            }

            String result_code = result.getResult_code();
            if (result_code.equalsIgnoreCase("FAIL")) {
                //订单编号
                String out_trade_no = result.getOut_trade_no();
                logger.error("订单" + out_trade_no + "支付失败");
                response.getWriter().write(setXml("SUCCESS", "OK"));
            } else if (result_code.equalsIgnoreCase("SUCCESS")) {
                Map<Object, Object> retMap = XmlUtil.xmlStrToTreeMap(reponseXml);
                String sign = WechatUtil.arraySign(retMap, ResourceUtil.getConfigByName("wx.paySignKey"));
                if(!sign.equals(result.getSign())) {//判断签名
                    return;
                }

                // 更改订单状态
                // 业务处理
                OrderVo orderInfo = orderService.findOrder(result.getOut_trade_no());
                orderInfo.paid(orderInfo);

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


                response.getWriter().write(setXml("SUCCESS", "OK"));
            }
        } catch (Exception e) {
            e.printStackTrace();
            return;
        }
    }

    public static String setXml(String return_code, String return_msg) {
        return "<xml><return_code><![CDATA[" + return_code + "]]></return_code><return_msg><![CDATA[" + return_msg + "]]></return_msg></xml>";
    }

}
