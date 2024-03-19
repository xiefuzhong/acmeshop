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
import com.acme.acmemall.model.enums.RefundOptionEnum;
import com.acme.acmemall.service.IOrderRefundService;
import com.acme.acmemall.utils.wechat.WechatRefundApiResult;
import com.acme.acmemall.utils.wechat.WechatUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @description:
 * @author: ihpangzi
 * @time: 2024/2/28 20:26
 */
@Slf4j
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
        OrderVo orderVo = orderMapper.queryObject(request.getOrderId());
        if (orderVo == null) {
            return ResultMap.error("订单不存在");
        }
        if (!orderVo.checkOwner(loginUser.getUserId())) {
            return ResultMap.error("订单不属于该用户");
        }
        OrderRefundVo refundVo = orderRefundMapper.findByOrderId(request.getOrderId());
        if (refundVo == null) {
            refundVo = OrderRefundFactory.build(request, loginUser.getUserId());
            orderVo.refundRequest(request);
        } else if (!refundVo.canApply()) {
            return ResultMap.error("售后中不能重复提交");
        }
        if (refundVo == null) {
            return ResultMap.error("无售后信息");
        }
        log.info("request.getRefundOption: {}", request.getRefundOption());
        log.info("orderVo.afterService before: {}", orderVo);
        orderVo.afterService(refundVo, request.getRefundOption());
        log.info("orderVo.afterService after: {}", orderVo);
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
        refundVo.updateRequest(request);
        OrderVo orderVo = orderMapper.queryObject(request.getOrderId());
        // 退款操作
        RefundOptionEnum refundOption = RefundOptionEnum.parse(request.getRefundOption());
        if (refundOption == RefundOptionEnum.REFUND) {
            log.info("refundOption: {},进行退款操作：{},refundVo:{}", refundOption, orderVo.getId(), refundVo);
            if (!orderVo.canRefund(refundVo)) {
                return ResultMap.error("状态不对,不能退款");
            }
            WechatRefundApiResult result = WechatUtil.wxRefund(orderVo.getId(), orderVo.getActual_price().doubleValue(), refundVo.getRefund_price().doubleValue());
            if (StringUtils.equalsIgnoreCase("SUCCESS", result.getResult_code())) {
                orderVo.afterService(refundVo, request.getRefundOption());
                orderMapper.update(orderVo);
                orderRefundMapper.update(orderVo.getRefundVo());
                return ResultMap.ok("操作成功，请查看账户");
            }
            return ResultMap.error(result.getErr_code_des());
        }

        orderVo.afterService(refundVo, request.getRefundOption());
        orderMapper.update(orderVo);
        orderRefundMapper.update(orderVo.getRefundVo());
        return ResultMap.response(ResultCodeEnum.SUCCESS, refundVo);
    }
}
