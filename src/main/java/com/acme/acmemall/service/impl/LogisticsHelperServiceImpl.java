package com.acme.acmemall.service.impl;

import com.acme.acmemall.model.LogisticsOrder;
import com.acme.acmemall.service.ILogisticsHelperService;
import com.acme.acmemall.service.ITokenService;
import com.acme.acmemall.utils.MapUtils;
import com.acme.acmemall.utils.UserUtils;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import javax.annotation.Resource;
import java.util.Map;

/**
 * @description:物流助手
 * @author: ihpangzi
 * @time: 2024/1/23 21:04
 */
@Service
public class LogisticsHelperServiceImpl implements ILogisticsHelperService {

    @Resource
    ITokenService tokenService;

    @Resource
    RestTemplate restTemplate;

    protected Logger logger = Logger.getLogger(getClass());


    /**
     * @param order
     * @return
     */
    @Override
    public Object addOrder(LogisticsOrder order, long userId) {
        Map result = tokenService.getTokens(userId);
        String accessToken = MapUtils.getString("token", result);
        String requestUrl = UserUtils.getWxAddLogisticsOrder(accessToken);
        String res = restTemplate.getForObject(requestUrl, String.class);
        logger.info("res==" + res);
        return res;
    }

    public Object getQuota(long userId) {
        Map result = tokenService.getTokens(userId);
        String accessToken = MapUtils.getString("token", result);
        String requestUrl = UserUtils.getWxLogistisQuota(accessToken);
        String res = restTemplate.getForObject(requestUrl, String.class);
        logger.info("res==" + res);
        return res;
    }
}
