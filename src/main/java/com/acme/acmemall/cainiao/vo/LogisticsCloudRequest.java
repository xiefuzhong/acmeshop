/*
 * Copyright (c) 2024. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */

package com.acme.acmemall.cainiao.vo;

import com.acme.acmemall.utils.MD5Util;
import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Maps;
import lombok.*;

import java.io.Serializable;
import java.util.Map;

/**
 * @description:
 * @author: ihpangzi
 * @time: 2024/5/10 21:45
 */

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
public class LogisticsCloudRequest implements Serializable {
    private String appid;
    private String sign;
    private String requestData;

    public void buildParams(String appSecret, String expressNo) {
        Map<String, String> requestParams = Maps.newHashMap();
        requestParams.put("mailNo", expressNo);
        String requestData = JSONObject.toJSONString(requestParams);
        this.requestData = requestData;
        String origin = requestData.concat(appSecret);
        String sign = MD5Util.MD5Encode(origin, "utf8");
        this.sign = sign;
    }

    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer("LogisticsCloudRequest{");
        sb.append("requestData='").append(requestData).append('\'');
        sb.append('}');
        return sb.toString();
    }
}
