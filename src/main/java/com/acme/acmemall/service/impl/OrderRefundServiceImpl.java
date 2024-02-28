package com.acme.acmemall.service.impl;

import com.acme.acmemall.common.ResultMap;
import com.acme.acmemall.controller.reqeust.OrderRefundRequest;
import com.acme.acmemall.dao.OrderRefundMapper;
import com.acme.acmemall.exception.ResultCodeEnum;
import com.acme.acmemall.factory.OrderRefundFactory;
import com.acme.acmemall.model.LoginUserVo;
import com.acme.acmemall.model.OrderRefundVo;
import com.acme.acmemall.service.IOrderRefundService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @description:
 * @author: ihpangzi
 * @time: 2024/2/28 20:26
 */
@Service
public class OrderRefundServiceImpl implements IOrderRefundService {

    @Resource
    OrderRefundMapper orderRefundMapper;

    /**
     * @param request
     * @param loginUser
     * @return
     */
    @Override
    public ResultMap submit(OrderRefundRequest request, LoginUserVo loginUser) {
        OrderRefundVo refundVo = OrderRefundFactory.build(request, loginUser);
        refundVo.submit();

        return ResultMap.response(ResultCodeEnum.SUCCESS, refundVo);
    }
}
