package com.acme.acmemall.service.impl;

import com.acme.acmemall.common.ResultMap;
import com.acme.acmemall.controller.reqeust.OrderRefundRequest;
import com.acme.acmemall.dao.OrderMapper;
import com.acme.acmemall.dao.OrderRefundMapper;
import com.acme.acmemall.exception.ResultCodeEnum;
import com.acme.acmemall.factory.OrderRefundFactory;
import com.acme.acmemall.model.CapitalFlowVo;
import com.acme.acmemall.model.LoginUserVo;
import com.acme.acmemall.model.OrderRefundVo;
import com.acme.acmemall.model.OrderVo;
import com.acme.acmemall.model.enums.PayType;
import com.acme.acmemall.model.enums.RefundOptionEnum;
import com.acme.acmemall.model.enums.TradeType;
import com.acme.acmemall.service.IFinanceFowService;
import com.acme.acmemall.service.IOrderRefundService;
import com.acme.acmemall.utils.SnowFlakeGenerateIdWorker;
import com.acme.acmemall.utils.wechat.WechatRefundApiResult;
import com.acme.acmemall.utils.wechat.WechatUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

    @Resource
    IFinanceFowService financeFowService;

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
//            orderVo.refundRequest(request);
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
    @Transactional(rollbackFor = {RuntimeException.class, Exception.class})
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
                // TODO: 2024/4/26 微信退款成功后，需要给用户发送通知,这里暂时不做处理。记录资金流水即可
                // 记录资金流水
                String flowId = SnowFlakeGenerateIdWorker.generateId();
                CapitalFlowVo flowVo = CapitalFlowVo.builder()
                        .flow_id(String.format("TR%s", flowId))
                        .order_id(orderVo.getId())
                        .trade_type(TradeType.ORDER_REFUND.getType())
                        .trade_amount(refundVo.getRefund_price())
                        .pay_type(PayType.WECHAT.getCode())
                        .add_time(System.currentTimeMillis() / 1000)
                        .user_id(refundVo.getUser_id())
                        .remark("微信退款")
                        .build();
                financeFowService.saveCapitalFlow(flowVo);
                log.info("微信退款成功，资金流水记录成功");
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
