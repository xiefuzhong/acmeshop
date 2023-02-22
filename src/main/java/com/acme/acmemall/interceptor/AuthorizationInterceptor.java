package com.acme.acmemall.interceptor;

import com.acme.acmemall.annotation.IgnoreAuth;
import com.acme.acmemall.exception.ApiCusException;
import com.acme.acmemall.model.LoginUserVo;
import com.acme.acmemall.service.IUserService;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 权限(Token)验证
 *
 * @author admin
 * @date 2017-03-23 15:38
 */
@Component
public class AuthorizationInterceptor extends HandlerInterceptorAdapter {
    @Autowired
    private IUserService userService;

    public static final String LOGIN_USER_KEY = "LOGIN_USER_KEY";
    public static final String LOGIN_TOKEN_KEY = "X-Acme-Token";

    private final Logger logger = Logger.getLogger(getClass());


    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        //支持跨域请求
        response.setHeader("Access-Control-Allow-Methods", "POST, GET, OPTIONS, DELETE");
        response.setHeader("Access-Control-Max-Age", "3600");
        response.setHeader("Access-Control-Allow-Credentials", "true");
        response.setHeader("Access-Control-Allow-Headers", "x-requested-with");
        response.setHeader("Access-Control-Allow-Origin", request.getHeader("Origin"));


        IgnoreAuth annotation;
        if (handler instanceof HandlerMethod) {
            annotation = ((HandlerMethod) handler).getMethodAnnotation(IgnoreAuth.class);
        } else {
            return true;
        }

        //从header中获取token
        String openId = request.getHeader(LOGIN_TOKEN_KEY);
        //如果header中不存在token，则从参数中获取token
        if (StringUtils.isBlank(openId)) {
            openId = request.getParameter(LOGIN_TOKEN_KEY);
        }
        logger.info("operId="+openId);
        //token为空
        if (StringUtils.isBlank(openId)) {
            // 如果有@IgnoreAuth注解，则不验证openId
            if (annotation != null) {
                return true;
            } else {
                logger.info("open id is null");
                throw new ApiCusException("请先登录", 401);
            }
        }
        if (request.getAttribute(LOGIN_USER_KEY) == null) {
            if (openId.startsWith("weglapp_")) {//app登录
//				MlsUserEntity2 mlsUser=mlsUserSer.getEntityMapper().findByDeviceId(openId);
//				if(mlsUser==null&&annotation == null) {
//					throw new ApiCusException("请先登录", 401);
//				}
//		        //设置userId到request里，后续根据userId，获取用户信息
//		        request.setAttribute(LOGIN_USER_KEY, mlsUser);
            } else {
                LoginUserVo userVo = userService.queryByOpenId(openId);
                if (userVo == null && annotation == null) {
                    logger.info("queryByOpenId return null");
//                    throw new ApiCusException("请先登录", 401);
                }
                //设置userId到request里，后续根据userId，获取用户信息
                request.setAttribute(LOGIN_USER_KEY, userVo);
            }
        }
        return true;
    }
}
