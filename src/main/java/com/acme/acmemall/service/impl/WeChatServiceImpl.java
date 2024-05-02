package com.acme.acmemall.service.impl;

import com.acme.acmemall.service.IWeChatService;
import com.google.common.base.Throwables;
import com.google.common.collect.Maps;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import javax.annotation.Resource;
import java.util.Map;

/**
 * @description:
 * @author: ihpangzi
 * @time: 2024/3/21 12:50
 */
@Slf4j
@Service
public class WeChatServiceImpl implements IWeChatService {

    @Resource
    RestTemplate restTemplate;

    /**
     * 获取用户手机号
     *
     * @param code 查询code
     * @return 获取用户手机号
     */
    @Override
    public String getUserPhoneNumber(String url, String code) {
        Map<String, String> params = Maps.newHashMap();
        params.put("code", code);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<Map<String, String>> request = new HttpEntity<>(params, headers);
        String response = null;
        try {
            response = restTemplate.postForObject(url, request, String.class);
        } catch (RestClientException e) {
            log.error("查询报错:" + Throwables.getStackTraceAsString(e));
        }
        return response;
    }

    /**
     * 生成运单（物流助手）
     *
     * @param requestUrl
     * @param param
     * @return
     */
    @Override
    public String addOrder(String requestUrl, Map<String, Object> param) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<Map<String, Object>> request = new HttpEntity<>(param, headers);
        String response = null;
        try {
            response = restTemplate.postForObject(requestUrl, request, String.class);
        } catch (RestClientException e) {
            log.error("查询报错:" + Throwables.getStackTraceAsString(e));
        }
        return response;
    }

    /**
     * @param requestUrl
     * @return
     */
    @Override
    public String getAllDelivery(String requestUrl) {
        String response = null;
        try {
            response = restTemplate.getForObject(requestUrl, String.class);
        } catch (RestClientException e) {
            log.error("查询报错:" + Throwables.getStackTraceAsString(e));
        }
        return response;
    }

    /**
     * @param requestUrl
     * @return
     */
    @Override
    public String getExpressAccount(String requestUrl) {
        String response = null;
        try {
            response = restTemplate.getForObject(requestUrl, String.class);
        } catch (RestClientException e) {
            log.error("查询报错:" + Throwables.getStackTraceAsString(e));
        }
        return response;
    }

}
