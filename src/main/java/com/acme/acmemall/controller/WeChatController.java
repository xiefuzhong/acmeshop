package com.acme.acmemall.controller;

import com.acme.acmemall.annotation.LoginUser;
import com.acme.acmemall.model.LoginUserVo;
import com.acme.acmemall.service.ITokenService;
import com.acme.acmemall.service.IWeChatService;
import com.acme.acmemall.utils.MapUtils;
import com.acme.acmemall.utils.UserUtils;
import com.google.common.collect.Maps;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
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

    /**
     * 获取用户手机号
     *
     * @param loginUser 登录用户
     * @param code      code
     * @return 获取用户手机号
     */
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
        Map param = Maps.newHashMap();
        String res = weChatService.addOrder(requestUrl, param);
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
}
