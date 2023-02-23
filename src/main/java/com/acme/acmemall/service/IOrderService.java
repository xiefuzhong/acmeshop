package com.acme.acmemall.service;

import com.acme.acmemall.common.ResultMap;
import com.acme.acmemall.controller.reqeust.OrderSubmitRequest;
import com.acme.acmemall.model.LoginUserVo;

/**
 * @description:购物车功能
 * @author: ihpangzi
 * @time: 2023/2/13 11:20
 */
public interface IOrderService {
    /**
     * 订单提交
     *
     * @param request 订单提交请求参数
     * @param loginUser   登录用户信息
     * @return 订单提交结果
     */
    ResultMap submit(OrderSubmitRequest request, LoginUserVo loginUser);
}
