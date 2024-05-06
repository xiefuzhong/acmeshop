package com.acme.acmemall.service;

import java.util.Map;

public interface IWeChatService {

    /**
     * 获取用户手机号
     *
     * @param code 查询code
     * @return 获取用户手机号
     */
    String getUserPhoneNumber(String url, String code);

    /**
     * 生成运单（物流助手）
     *
     * @param requestUrl
     * @param param
     * @return
     */
    String addOrder(String requestUrl, Map<String, Object> param);

    String getAllDelivery(String requestUrl);

    String getExpressAccount(String requestUrl);

    String getPath(String requestUrl, Map<String, Object> param);
}
