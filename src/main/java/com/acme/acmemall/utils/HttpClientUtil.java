/*
 * Copyright (c) 2024. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */

package com.acme.acmemall.utils;

import com.google.common.base.Throwables;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpResponse;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @description:
 * @author: ihpangzi
 * @time: 2024/5/10 19:31
 */

@Slf4j
public class HttpClientUtil {


    /**
     * 以form表单形式提交数据，发送post请求
     *
     * @param url
     * @param params
     * @param headers
     * @return
     */
    public static String httpPostWithForm(String url, Map<String, Object> params, Map<String, String> headers) {
        // 用于接收返回的结果
        String resultData = "";
        try {
            HttpPost post = new HttpPost(url);
            //设置头部信息
            if (headers != null && !headers.isEmpty()) {
                for (Map.Entry<String, String> entry : headers.entrySet()) {
                    post.setHeader(entry.getKey(), entry.getValue());
                }
            }
            List<BasicNameValuePair> pairList = new ArrayList<>();
            for (String key : params.keySet()) {
                pairList.add(new BasicNameValuePair(key, StringUtils.toStringByObject(params.get(key))));
            }
            UrlEncodedFormEntity uefe = new UrlEncodedFormEntity(pairList, "utf-8");
            post.setEntity(uefe);
            // 创建一个http客户端
            CloseableHttpClient httpClient = HttpClientBuilder.create().build();
            // 发送post请求
            HttpResponse response = httpClient.execute(post);
            resultData = EntityUtils.toString(response.getEntity(), "UTF-8");// 返回正常数据
        } catch (Exception e) {
            log.error("接口连接失败 e:" + Throwables.getStackTraceAsString(e));
        }
        return resultData;
    }
}
