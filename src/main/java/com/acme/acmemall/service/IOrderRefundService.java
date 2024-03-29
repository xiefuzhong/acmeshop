package com.acme.acmemall.service;

import com.acme.acmemall.common.ResultMap;
import com.acme.acmemall.controller.reqeust.OrderRefundRequest;
import com.acme.acmemall.model.LoginUserVo;
import com.acme.acmemall.model.OrderRefundVo;

public interface IOrderRefundService {
    ResultMap submit(OrderRefundRequest request, LoginUserVo loginUser);

    OrderRefundVo findByOrderId(String orderId);

    ResultMap updateRefund(OrderRefundRequest request);
}
