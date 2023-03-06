package com.acme.acmemall.controller;

import com.acme.acmemall.annotation.LoginUser;
import com.acme.acmemall.common.ResultMap;
import com.acme.acmemall.model.LoginUserVo;
import com.acme.acmemall.model.OrderVo;
import com.acme.acmemall.service.IOrderService;
import com.acme.acmemall.utils.CharUtil;
import com.acme.acmemall.utils.MapUtils;
import com.acme.acmemall.utils.ResourceUtil;
import com.acme.acmemall.utils.XmlUtil;
import com.acme.acmemall.utils.wechat.WechatUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
            String sign = WechatUtil.arraySign(parame, WechatUtil.getPrivatekey(ResourceUtil.getConfigByName("wx.paySignKey")));
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
                    newOrder.paid(newOrder);
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

}
