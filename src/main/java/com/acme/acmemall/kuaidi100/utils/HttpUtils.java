package com.acme.acmemall.kuaidi100.utils;

import org.apache.http.NameValuePair;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;


public class HttpUtils {

    private final static String CHARSET_DEFAULT = "UTF-8";

    /**
     * post请求  编码格式默认UTF-8
     *
     * @param url 请求url
     * @return
     */
    public static HttpResult doPost(String url, Object obj, int connectTimeout, int socketTimeout) throws IOException, IllegalAccessException {
        CloseableHttpClient httpClient = HttpClientBuilder.create().build();
        CloseableHttpResponse resp = null;

        HttpResult result = new HttpResult();
        try {
            Map<String, String> params = ObjectToMapUtils.objectToMap(obj);
            HttpPost httpPost = new HttpPost(url);

            RequestConfig requestConfig = RequestConfig.custom()
                    .setConnectTimeout(connectTimeout)
                    .setSocketTimeout(socketTimeout).build();
            httpPost.setConfig(requestConfig);
            if (params != null && params.size() > 0) {
                List<NameValuePair> list = new ArrayList<NameValuePair>();
                for (Map.Entry<String, String> entry : params.entrySet()) {
                    list.add(new BasicNameValuePair(entry.getKey(), entry.getValue()));
                }
                httpPost.setEntity(new UrlEncodedFormEntity(list, CHARSET_DEFAULT));
            }

            resp = httpClient.execute(httpPost);
            String body = EntityUtils.toString(resp.getEntity(), CHARSET_DEFAULT);
            int statusCode = resp.getStatusLine().getStatusCode();
            result.setStatus(statusCode);
            result.setBody(body);
        } finally {
            if (null != resp) {
                resp.close();
            }
        }
        return result;
    }
}
