package com.acme.acmemall.service;

public interface IWeChatService {

    /**
     * 获取用户手机号
     *
     * @param code 查询code
     * @return 获取用户手机号
     */
    String getUserPhoneNumber(String url, String code);
}
