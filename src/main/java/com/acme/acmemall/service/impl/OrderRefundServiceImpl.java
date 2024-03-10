package com.acme.acmemall.service.impl;

import com.acme.acmemall.common.ResultMap;
import com.acme.acmemall.controller.reqeust.OrderRefundRequest;
import com.acme.acmemall.dao.OrderMapper;
import com.acme.acmemall.dao.OrderRefundMapper;
import com.acme.acmemall.exception.ResultCodeEnum;
import com.acme.acmemall.factory.OrderRefundFactory;
import com.acme.acmemall.model.LoginUserVo;
import com.acme.acmemall.model.OrderRefundVo;
import com.acme.acmemall.model.OrderVo;
import com.acme.acmemall.service.IOrderRefundService;
import com.acme.acmemall.utils.wechat.WechatRefundApiResult;
import com.acme.acmemall.utils.wechat.WechatUtil;
import org.apache.commons.lang.StringUtils;
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

    @Resource
    OrderMapper orderMapper;

    /**
     * @param request
     * @param loginUser
     * @return
     */
    @Override
    public ResultMap submit(OrderRefundRequest request, LoginUserVo loginUser) {
        OrderRefundVo refundVo = OrderRefundFactory.build(request);
        refundVo.submit(loginUser.getUserId());
        OrderVo orderVo = orderMapper.queryObject(request.getOrderId());
        orderVo.afterService(refundVo);
        orderMapper.update(orderVo);
        orderRefundMapper.save(refundVo);
        return ResultMap.response(ResultCodeEnum.SUCCESS, refundVo);
    }

    /**
     * @param orderId
     * @return
     */
    @Override
    public OrderRefundVo findByOrderId(String orderId) {
        return orderRefundMapper.findByOrderId(orderId);
    }

    /**
     * @param request
     * @return
     */
    @Override
    public ResultMap updateRefund(OrderRefundRequest request) {
        OrderRefundVo refundVo = orderRefundMapper.findByOrderId(request.getOrderId());
        if (refundVo == null) {
            return ResultMap.error("无售后信息");
        }
        OrderVo orderVo = orderMapper.queryObject(request.getOrderId());
        orderVo.updateRefund(request);
        if (orderVo.canRefund()) {
            // 订单金额
            Double orderMoney = orderVo.getActual_price().doubleValue();
            // 退款金额
            Double refundMoney = refundVo.getRefund_price().doubleValue();
            WechatRefundApiResult result = WechatUtil.wxRefund(orderVo.getId(), orderMoney, refundMoney);
            if (StringUtils.equalsIgnoreCase("SUCCESS", result.getResult_code())) {
                refundVo.refundPaid();
                orderVo.closed(refundVo);
            }
        }
        orderMapper.update(orderVo);
        orderRefundMapper.update(refundVo);
        return ResultMap.response(ResultCodeEnum.SUCCESS, refundVo);
    }
}
