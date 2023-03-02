package com.acme.acmemall.controller;

import com.acme.acmemall.annotation.IgnoreAuth;
import com.acme.acmemall.model.LoginInfo;
import com.acme.acmemall.model.LoginUserVo;
import com.acme.acmemall.service.ITokenService;
import com.acme.acmemall.service.IUserService;
import com.acme.acmemall.utils.Base64;
import com.acme.acmemall.utils.StringUtils;
import com.acme.acmemall.utils.UserUtils;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Maps;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.collections.MapUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.Map;

/**
 * API登录授权
 */
@Api(tags = "API登录授权接口")
@RestController
@RequestMapping("/api/auth")
public class AuthController extends ApiBase {
    @Autowired
    private IUserService userService;
    @Autowired
    private ITokenService tokenService;
    @Autowired
    private RestTemplate restTemplate;
//    @Autowired
//	private MlsUserSer mlsUserSer;

    /**
     * 微信授权登录
     */
    @ApiOperation(value = "微信授权登录")
    @IgnoreAuth
    @PostMapping("login_by_weixin")
    public Object loginByWeixin(@RequestBody LoginInfo loginInfo, HttpServletRequest request) {
        logger.info("loginByWeixin.loginInfo>>"+JSONObject.toJSON(loginInfo));
        //获取openid
        String requestUrl = UserUtils.getWebAccess(loginInfo.getCode());//通过自定义工具类组合出小程序需要的登录凭证 code
        logger.info("》》》requestUrl为：" + requestUrl);
        String res = restTemplate.getForObject(requestUrl, String.class);
        logger.info("res==" + res);
        JSONObject sessionData = JSON.parseObject(res);
        // {"session_key":"GhiV7gQt9PYZc5OTo\/lp8Q==","openid":"oSjwN5S9p3sk02PXauTTz3TR1zP0"}
        String openid = sessionData.getString("openid");
        logger.info("》》》promoterId：" + loginInfo.getPromoterId());
        String session_key = sessionData.getString("session_key");// 用于解密 getUserInfo返回的敏感数据。
        if (null == sessionData || StringUtils.isNullOrEmpty(openid)) {
            logger.error("session_key>>" + session_key);
            return toResponsFail("登录失败");
        }
        //验证用户信息完整性 防止攻击
//        String sha1 = CommonUtil.getSha1(fullUserInfo.getRawData() + sessionData.getString("session_key"));
//        if (!fullUserInfo.getSignature().equals(sha1)) {
//        	 logger.info("登录失败---验证用户信息完整性"+fullUserInfo.getSignature());
//        	 logger.info("登录失败---验证用户信息完整性 sha1"+sha1);
//            return toResponsFail("登录失败");
//        }
        Date nowTime = new Date();
        LoginUserVo userVo = userService.queryByOpenId(openid);
        logger.info("LoginUserVo>>" +JSONObject.toJSON(userVo));
        if (null == userVo) {
            userVo = new LoginUserVo();
            userVo.setUsername(Base64.encode(loginInfo.getNickName()));
            userVo.setPassword(openid);
            userVo.setRegister_time(nowTime);
            userVo.setRegister_ip(this.getClientIp());
            userVo.setLast_login_ip(userVo.getRegister_ip());
            userVo.setLast_login_time(userVo.getRegister_time());
            userVo.setWeixin_openid(openid);
            userVo.setAvatar(loginInfo.getAvatarUrl());
            userVo.setGender(loginInfo.getGender()); // //性别 0：未知、1：男、2：女
            userVo.setNickname(Base64.encode(loginInfo.getNickName()));
            userVo.setPromoterId(loginInfo.getPromoterId());
            // 保存授权登录信息
            userService.save(userVo);
            logger.info("userId>>"+userVo.getUserId());
        }
        logger.info("save.after.userVo>>"+JSONObject.toJSON(userVo));
        Map<String, Object> tokenMap = tokenService.createToken(userVo.getUserId());
        String token = MapUtils.getString(tokenMap, "token");

        if (StringUtils.isNullOrEmpty(token)) {
            logger.info("createToken fail!");
            return toResponsFail("登录失败");
        }
        Map<String, Object> resultObj = Maps.newHashMap();
        resultObj.put("openid", openid);
        resultObj.put("userVo", userVo);
        resultObj.put("session_key",session_key);
        return toResponsSuccess(resultObj);
    }
}
