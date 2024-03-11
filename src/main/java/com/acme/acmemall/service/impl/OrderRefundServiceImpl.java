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

    //    private static final Logger LOGGER = Logger.getLogger(getClass());
//    private static final Logger LOGGER = LoggerFactory.getLogger(OrderRefundServiceImpl.class);

    /**
     * @param request
     * @param loginUser
     * @return
     */
    @Override
    public ResultMap submit(OrderRefundRequest request, LoginUserVo loginUser) {
        OrderRefundVo orderRefundVo = orderRefundMapper.findByOrderId(request.getOrderId());
        if (orderRefundVo != null && !orderRefundVo.canApply()) {
            return ResultMap.error("售后中不能重复提交");
        }
        OrderRefundVo refundVo = OrderRefundFactory.build(request);
        OrderVo orderVo = orderMapper.queryObject(request.getOrderId());
        orderVo.afterService(refundVo, request.getRefundOption());
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
        if (orderVo.canRefund(refundVo)) {
            RefundOptionEnum refundOption = RefundOptionEnum.parse(request.getRefundOption());
            if (refundOption == RefundOptionEnum.REFUND) {
                WechatRefundApiResult result = WechatUtil.wxRefund(orderVo.getId(), orderVo.getActual_price().doubleValue(), refundVo.getRefund_price().doubleValue());
                if (StringUtils.equalsIgnoreCase("SUCCESS", result.getResult_code())) {
                    orderVo.afterService(refundVo, request.getRefundOption());
                    log.info("orderVo.updateRefund after: {}", orderVo);
                    orderMapper.update(orderVo);
                    orderRefundMapper.update(orderVo.getRefundVo());
                    return ResultMap.ok("操作成功，请查看账户");
                }
            }
        }
        orderVo.afterService(refundVo, request.getRefundOption());
        log.info("orderVo.updateRefund after: {}", orderVo);
        orderMapper.update(orderVo);
        orderRefundMapper.update(orderVo.getRefundVo());
        return ResultMap.response(ResultCodeEnum.SUCCESS, refundVo);
    }
}
