package com.acme.acmemall.controller;

import com.acme.acmemall.annotation.IgnoreAuth;
import com.acme.acmemall.annotation.LoginUser;
import com.acme.acmemall.model.AddressVo;
import com.acme.acmemall.model.LoginUserVo;
import com.acme.acmemall.model.LogisticsOrder;
import com.acme.acmemall.model.OrderVo;
import com.acme.acmemall.service.IAddressService;
import com.acme.acmemall.service.IOrderService;
import com.acme.acmemall.service.ITokenService;
import com.acme.acmemall.service.IWeChatService;
import com.acme.acmemall.utils.MapUtils;
import com.acme.acmemall.utils.UserUtils;
import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Maps;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * @description:
 * @author: ihpangzi
 * @time: 2024/2/26 16:52
 */
@RestController
@RequestMapping("/api/wechat")
public class WeChatController extends ApiBase {

    @Resource
    IWeChatService weChatService;

    @Resource
    ITokenService tokenService;

    @Resource
    IOrderService orderService;

    @Resource
    IAddressService addressService;

    /**
     * 获取用户手机号
     *
     * @param loginUser 登录用户
     * @param code      code
     * @return 获取用户手机号
     */
    @IgnoreAuth
    @GetMapping("/get-mobile")
    public Object getUserPhoneNumber(@LoginUser LoginUserVo loginUser, @RequestParam("code") String code) {
        logger.info("》》》获取用户手机号>>>getUserPhoneNumber");
        Map result = tokenService.getTokens(loginUser.getUserId());
        String accessToken = MapUtils.getString("token", result);
        Map<String, Object> param = Maps.newHashMap();
        param.put("code", code);
        String requestUrl = UserUtils.getUserPhoneNumber(accessToken);//通过自定义工具类组合出小程序需要的登录凭证 code
        logger.info("》》》requestUrl为：" + requestUrl);
        String res = weChatService.getUserPhoneNumber(requestUrl, code);
        return toResponsSuccess(res);
    }

    @PostMapping("/logistics/add-order")
    public Object addOrder(@LoginUser LoginUserVo loginUser) {
        logger.info("》》》物流助手>>>addOrder");
        Map result = tokenService.getTokens(loginUser.getUserId());
        String accessToken = MapUtils.getString("token", result);
        String requestUrl = UserUtils.getWxAddLogisticsOrder(accessToken);
        logger.info("》》》requestUrl为：" + requestUrl);
        JSONObject json = getJsonRequest();
        logger.info("》》》json为：" + json);
        OrderVo orderVo = orderService.findOrder(json.getString("orderId"));
        LogisticsOrder logisticsOrder = LogisticsOrder.builder()
                .order_id(json.getString("orderId"))
                .add_source(0) // 小程序
                .delivery_id(json.getString("delivery_id"))
                .biz_id(json.getString("biz_id"))
                .openid(loginUser.getWeixin_openid())
//                .tagid(json.getLong("merchantId"))
                .custom_remark(orderVo.getGoods_name() + "_" + orderVo.getMerRemark())
                .build();

//        logger.info("》》》orderVo为：" + orderVo);
        // 发货地址
        Map param = Maps.newHashMap();
        param.put("user_id", json.getLong("merchantId"));
        param.put("type", Integer.valueOf(0));
        List<AddressVo> addressEntities = addressService.queryaddressUserlist(param);
//        logger.info("》》》addressEntities为：" + addressEntities);
        logisticsOrder.addOrder(orderVo, addressEntities.get(0), json);
        Map requestParams = MapUtils.beanToMap(logisticsOrder);
        String res = weChatService.addOrder(requestUrl, requestParams);
        return toResponsSuccess(res);
    }

    @GetMapping("/logistics/account-get")
    public Object getAccount(@LoginUser LoginUserVo loginUser) {
        logger.info("》》》物流助手>>>getAccount");
        Map result = tokenService.getTokens(loginUser.getUserId());
        String accessToken = MapUtils.getString("token", result);
        String requestUrl = UserUtils.getExpressAccount(accessToken);
        logger.info("》》》requestUrl为：" + requestUrl);
        String res = weChatService.getExpressAccount(requestUrl);
        return toResponsSuccess(res);
    }

    @GetMapping("/logistics/all-delivery")
    public Object getAllDelivery(@LoginUser LoginUserVo loginUser) {
        logger.info("》》》物流助手>>>getAllDelivery");
        Map result = tokenService.getTokens(loginUser.getUserId());
        String accessToken = MapUtils.getString("token", result);
        String requestUrl = UserUtils.getAllDelivery(accessToken);
        logger.info("》》》requestUrl为：" + requestUrl);
        String res = weChatService.getAllDelivery(requestUrl);
        return toResponsSuccess(res);
    }

    @PostMapping("/logistics/get-path")
    public Object getPath(@LoginUser LoginUserVo loginUser) {
        logger.info("》》》物流助手>>>getPath");
        Map result = tokenService.getTokens(loginUser.getUserId());
        String accessToken = MapUtils.getString("token", result);
        String requestUrl = UserUtils.getWxExpressTrack(accessToken);
        OrderVo orderVo = orderService.findOrder(getJsonRequest().getString("orderId"));
        if (orderVo == null) {
            return toResponsFail("订单不存在");
        }
        Map params = Maps.newHashMap();
        params.put("openid", loginUser.getWeixin_openid());
        params.put("delivery_id", orderVo.getShipping_code());
        params.put("waybill_id", orderVo.getShipping_no());
        String res = weChatService.getPath(requestUrl, params);
        return toResponsSuccess(res);
    }
}
