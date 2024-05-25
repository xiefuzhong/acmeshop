package com.acme.acmemall.controller;

import com.acme.acmemall.annotation.IgnoreAuth;
import com.acme.acmemall.annotation.LoginUser;
import com.acme.acmemall.common.ResultMap;
import com.acme.acmemall.exception.Assert;
import com.acme.acmemall.exception.ResultCodeEnum;
import com.acme.acmemall.model.LoginInfo;
import com.acme.acmemall.model.LoginUserVo;
import com.acme.acmemall.service.ITokenService;
import com.acme.acmemall.service.IUserService;
import com.acme.acmemall.service.IWeChatService;
import com.acme.acmemall.utils.GsonUtil;
import com.acme.acmemall.utils.StringUtils;
import com.acme.acmemall.utils.UserUtils;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Maps;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.collections.MapUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.Map;
import java.util.Objects;

/**
 * API登录授权
 */
@Api(tags = "API登录授权接口")
@RestController
@RequestMapping("/api/auth")
public class AuthController extends ApiBase {
    @Resource
    IUserService userService;
    @Resource
    ITokenService tokenService;
    @Resource
    RestTemplate restTemplate;

    @Resource
    IWeChatService weChatService;

    /**
     * 微信授权登录
     */
    @ApiOperation(value = "微信授权登录")
    @IgnoreAuth
    @PostMapping("login_by_weixin")
    public Object loginByWeixin(@RequestBody LoginInfo loginInfo, HttpServletRequest request) {
        logger.info("loginByWeixin.loginInfo>>" + JSONObject.toJSON(loginInfo));
        //获取openid
        String requestUrl = UserUtils.getWebAccess(loginInfo.getCode());//通过自定义工具类组合出小程序需要的登录凭证 code
        logger.info("》》》requestUrl为：" + requestUrl);
        String res = restTemplate.getForObject(requestUrl, String.class);
        logger.info("res==" + res);
        JSONObject sessionData = JSON.parseObject(res);
        String openid = Objects.requireNonNull(sessionData).getString("openid");
        logger.info("》》》promoterId：" + loginInfo.getPromoterId());
        String session_key = sessionData.getString("session_key");// 用于解密 getUserInfo返回的敏感数据。
        if (StringUtils.isNullOrEmpty(openid)) {
            logger.error("session_key>>" + session_key);
            return toResponsFail("登录失败");
        }
        LoginUserVo userVo = userService.queryByOpenId(openid);
        logger.info("LoginUserVo>>" + JSONObject.toJSON(userVo));
        if (null == userVo) {
            userVo = new LoginUserVo();
            userVo.setWeixin_openid(openid);
            userVo.loginByWeixin(loginInfo, this.getClientIp());
            // 保存授权登录信息
            userService.save(userVo);
            logger.info("userId>>" + userVo.getUserId());
        } else {
            // 更新登录时间，登录IP,
            LoginUserVo updateUserVo = new LoginUserVo();
            updateUserVo.setUserId(userVo.getUserId());
            updateUserVo.setLast_login_ip(this.getClientIp());
            updateUserVo.setLast_login_time(new Date());
            userService.updateUser(updateUserVo);
        }
        logger.info("save.after.userVo>>" + JSONObject.toJSON(userVo));
        Map<String, Object> tokenMap = tokenService.createToken(userVo.getUserId());
        String token = MapUtils.getString(tokenMap, "token");

        if (StringUtils.isNullOrEmpty(token)) {
            logger.info("createToken fail!");
            return toResponsFail("登录失败");
        }
        Map<String, Object> resultObj = Maps.newHashMap();
        resultObj.put("openid", openid);
        resultObj.put("userVo", userVo);
        resultObj.put("session_key", session_key);
        return toResponsSuccess(resultObj);
    }

    @IgnoreAuth
    @PostMapping("login")
    @ApiOperation(value = "登录接口")
    public ResultMap login() {
        JSONObject request = this.getJsonRequest();
        Assert.isBlank(request.getString("mobile"), "手机号不能为空!");
        Assert.isBlank(request.getString("password"), "密码不能为空!");
        //用户登录
        LoginUserVo userVo = userService.login(request.getString("mobile"), request.getString("password"));
        //生成token
        Map<String, Object> map = tokenService.createToken(userVo.getUserId());
        map.put("merchantId", userVo.getMerchantId());
        return ResultMap.ok(map);
    }

    @PostMapping("/token")
    public Object getToken() {
        JSONObject request = this.getJsonRequest();
        if (request == null) {
            return ResultMap.badArgument();
        }
        Map<String, Object> result = tokenService.getTokens(request.getLong("merchantId"));
        logger.info("getToken==" + GsonUtil.toJson(result));
        return toResponsSuccess(result);
    }

    @IgnoreAuth
    @PostMapping("/get-openid")
    public Object getOpenId() {
        JSONObject request = this.getJsonRequest();
        if (request == null) {
            return ResultMap.badArgument();
        }
        String code = request.getString("code");
        String requestUrl = UserUtils.getWebAccess(code);//通过自定义工具类组合出小程序需要的登录凭证 code

        logger.info("》》》requestUrl为：" + requestUrl);
        String res = restTemplate.getForObject(requestUrl, String.class);
        logger.info("res==" + res);
        JSONObject sessionData = JSON.parseObject(res);
        String openid = Objects.requireNonNull(sessionData).getString("openid");
        String session_key = sessionData.getString("session_key");// 用于解密 getUserInfo返回的敏感数据。
        if (StringUtils.isNullOrEmpty(openid)) {
            logger.error("session_key>>" + session_key);
            return toResponsFail("登录失败");
        }

        return toResponsSuccess(sessionData);
    }

    @PostMapping("/bind-phone")
    public Object bindPhone(@LoginUser LoginUserVo userVo) {
        logger.info("》》》获取用户手机号>>>bindPhone");
        if (userVo == null) {
            return ResultMap.response(ResultCodeEnum.USER_NOT_LOGIN);
        }
        JSONObject request = this.getJsonRequest();
        if (request == null) {
            return ResultMap.badArgument();
        }
        Map<String, Object> tokenMap = tokenService.getTokens(userVo.getUserId());
        String accessToken = com.acme.acmemall.utils.MapUtils.getString("token", tokenMap);
        String requestUrl = UserUtils.getUserPhoneNumber(accessToken);//通过自定义工具类组合出小程序需要的登录凭证 code
        logger.info("》》》requestUrl为：" + requestUrl);
        String res = weChatService.getUserPhoneNumber(requestUrl, request.getString("code"));
        logger.info("res==" + res);
        JSONObject phoneObj = JSONObject.parseObject(res);
        JSONObject phone_info = phoneObj.getJSONObject("phone_info");
        userVo.setMobile(phone_info.getString("phoneNumber"));
        userService.updateUser(userVo);
        return toResponsSuccess(userVo);
    }
}
