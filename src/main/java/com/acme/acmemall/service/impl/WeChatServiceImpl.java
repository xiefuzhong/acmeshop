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
}
