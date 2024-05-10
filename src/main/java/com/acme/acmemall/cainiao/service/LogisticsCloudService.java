/*
 * Copyright (c) 2024. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */

package com.acme.acmemall.cainiao.service;

import com.acme.acmemall.cainiao.config.LogisticsCloudProperties;
import com.acme.acmemall.cainiao.vo.LogisticsCloudRequest;
import com.acme.acmemall.utils.HttpClientUtil;
import com.acme.acmemall.utils.MD5Util;
import com.acme.acmemall.utils.MapUtils;
import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Maps;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;

/**
 * @description:
 * @author: ihpangzi
 * @time: 2024/5/10 19:23
 */
@Slf4j
public class LogisticsCloudService {

    private LogisticsCloudProperties properties;

    public LogisticsCloudProperties getProperties() {
        return properties;
    }

    public void setProperties(LogisticsCloudProperties properties) {
        this.properties = properties;
    }


    /**
     * 物流轨迹查询
     *
     * @param expressNo 快递单号
     * @return
     */
    public String queryExpressRoutes(String expressNo) {
        log.info("【菜鸟物流】查询快递信息，快递单号：{}", expressNo);
        LogisticsCloudRequest request = LogisticsCloudRequest.builder()
                .appid(properties.getAppId())
                .build();
        request.buildParams(properties.getAppSecret(), expressNo);
        log.info("【菜鸟物流】请求参数：{}", request);

        Map<String, Object> params = MapUtils.beanToMap(request);
        //请求头
        Map<String, String> headers = Maps.newHashMap();
        headers.put("Content-Type", "application/x-www-form-urlencoded;charset=utf-8");
        String resultData = HttpClientUtil.httpPostWithForm(properties.getQueryExpressRoutes(), params, headers);
        return resultData;
    }

    public String interceptPackage(String expressNo) {
        log.info("【菜鸟物流】拦截快递信息，快递单号：{}", expressNo);
        // 按顺序将参数拼接起来 如：requestData+appSecret
        Map<String, String> requestParams = Maps.newHashMap();
        requestParams.put("mailNo", expressNo);
        String origin = JSONObject.toJSONString(requestParams).concat(properties.getAppSecret());
        log.info("【菜鸟物流】请求参数：{}", origin);
        String sign = MD5Util.MD5Encode(origin, "utf8");
        Map<String, Object> params = Maps.newHashMap();
        params.put("appid", properties.getAppId());
        params.put("sign", sign);
        //请求头
        Map<String, String> headers = Maps.newHashMap();
        headers.put("Content-Type", "application/x-www-form-urlencoded;charset=utf-8");
        String resultData = HttpClientUtil.httpPostWithForm(properties.getInterceptPackage(), params, headers);
        return resultData;
    }
}
