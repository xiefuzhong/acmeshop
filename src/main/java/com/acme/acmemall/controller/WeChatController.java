package com.acme.acmemall.controller;

import com.acme.acmemall.annotation.LoginUser;
import com.acme.acmemall.model.LoginUserVo;
import com.acme.acmemall.service.ITokenService;
import com.acme.acmemall.service.IWeChatService;
import com.acme.acmemall.utils.MapUtils;
import com.acme.acmemall.utils.UserUtils;
import com.google.common.collect.Maps;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

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
}
