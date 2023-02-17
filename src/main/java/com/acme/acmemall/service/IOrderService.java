package com.acme.acmemall.service;

import com.acme.acmemall.model.LoginUserVo;
import com.alibaba.fastjson.JSONObject;

import java.util.Map;

/**
 * @description:购物车功能
 * @author: ihpangzi
 * @time: 2023/2/13 11:20
 */
public interface IOrderService {
    /**
     * 订单提交
     *
     * @param jsonRequest 请求参数
     * @param loginUser   登录用户信息
     * @return 订单提交结果
     */
    Map submit(JSONObject jsonRequest, LoginUserVo loginUser);
}
