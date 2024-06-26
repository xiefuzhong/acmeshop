package com.acme.acmemall.service.impl;

import com.acme.acmemall.common.ResultMap;
import com.acme.acmemall.controller.reqeust.OrderSubmitRequest;
import com.acme.acmemall.dao.*;
import com.acme.acmemall.exception.Assert;
import com.acme.acmemall.exception.ResultCodeEnum;
import com.acme.acmemall.factory.OrderFactory;
import com.acme.acmemall.model.*;
import com.acme.acmemall.model.enums.CouponStatusEnum;
import com.acme.acmemall.model.enums.OrderStatusEnum;
import com.acme.acmemall.service.IFinanceFowService;
import com.acme.acmemall.service.IOrderService;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @description:
 * @author: ihpangzi
 * @time: 2023/2/17 15:58
 */
@Service
public class OrderServiceImpl implements IOrderService {

    @Resource
    UserCouponMapper userCouponMapper;

    @Resource
    CouponMapper couponMapper;

    @Resource
    AddressMapper addressMapper;

    @Resource
    ShopCartMapper cartMapper;

    @Resource
    OrderMapper orderMapper;

    @Resource
    OrderGoodsMapper orderItemMapper;

    @Resource
    InvoiceTitleMapper invoiceHeaderMapper;

    @Resource
    OrderRefundMapper orderRefundMapper;

    @Resource
    CapitalFlowMapper capitalFlowMapper;

    @Resource
    IFinanceFowService financeFowService;

    protected Logger logger = Logger.getLogger(getClass());

    /**
     * 订单提交
     *
     * @param request   请求参数
     * @param loginUser 登录用户信息
     * @return 订单提交结果
     */
    @Transactional(rollbackFor = {RuntimeException.class, Exception.class})
    @Override
    public ResultMap submit(OrderSubmitRequest request, LoginUserVo loginUser) {
        logger.info(String.format("订单提交: %s", request.toString()));
        // 提交方式:cart-购物车提交 2-直接购买;其他-团购购买
        if (!StringUtils.equals("cart", request.getType())) {
            return ResultMap.error(101, "请先添加到购物车,暂不支持其他方式");
        }
        AddressVo addressVo = addressMapper.queryObject(request.getAddressId());
        logger.info(addressVo.toString());
        if (addressVo == null || !addressVo.getUserId().equals(loginUser.getUserId())) {
            return ResultMap.error(102, "请选择联系地址");
        }
        // 查询发票抬头
        InvoiceTitleVo invoiceHeaderVo = null;
        if (request.getHeaderId() > 0) {
            invoiceHeaderVo = invoiceHeaderMapper.queryObject(request.getHeaderId());
        }

        // 购物车明细校验
        if (StringUtils.isEmpty(request.getCartIds())) {
            return ResultMap.response(ResultCodeEnum.FAILED);
        }
        // 查询用户已选择的购物车中信息，用来重新计算校验
        Map paramMap = Maps.newHashMap();
        paramMap.put("user_id", loginUser.getUserId());
        paramMap.put("checked", 1);
        List<ShopCartVo> cartList = cartMapper.queryList(paramMap);
        if (CollectionUtils.isEmpty(cartList)) {
            logger.error("Order.submit-->cartList is empty");
            return ResultMap.response(ResultCodeEnum.FAILED);
        }

        CouponVo couponVo = null;
        if (request.getUserCouponId() > 0) {
            couponVo = couponMapper.queryObject(request.getUserCouponId());
            Assert.isNull(couponVo, "未查询到有效的优惠券");
        }

        OrderVo order = OrderFactory.buildNewOrder(loginUser.getUserId(), request);
        order.submit(couponVo, cartList, addressVo, invoiceHeaderVo);
        order.checkSubmit();
        logger.info("order.submit >> " + order);
        // 保存order以及明细表
        orderMapper.save(order);
        orderItemMapper.saveBatch(order.getItems());

        // 释放优惠券信息
        // 用户选择的优惠券信息
        if (request.getUserCouponId() > 0) {
            Map params = Maps.newHashMap();
            params.put("user_id", loginUser.getUserId());
            params.put("coupon_status", CouponStatusEnum.COUPON_AVAILABLE.getCode());
            params.put("couponIds", Lists.newArrayList(request.getUserCouponId()));
            params.put("selected", true);
            // 商品总额
            params.put("goodsTotalPrice", request.getGoodsTotalPrice());

            List<UserCouponVo> userCouponList = userCouponMapper.queryList(params);
            if (CollectionUtils.isEmpty(userCouponList)) {
                return ResultMap.badArgument("订单不能使用优惠券，优惠券信息错误或已使用");
            }
            UserCouponVo userCouponVo = userCouponList.get(0);
            userCouponVo.exchange(loginUser.getUserId(), order.getId());
            userCouponMapper.update(userCouponVo);
        }

        // 删除购物车
        List<String> productIds = cartList.stream().map(cartVo -> cartVo.getProduct_id().toString()).collect(Collectors.toList());
        cartMapper.deleteByUserAndProductIds(loginUser.getUserId(), productIds.toArray(new String[productIds.size()]));
        return ResultMap.response(ResultCodeEnum.SUCCESS, order);
    }

    /**
     * @param params
     * @return
     */
    @Override
    public List<OrderVo> queryOrderList(Map params) {
        // 根据商户查
        if (params.containsKey("merchant_id")) {
            return orderMapper.queryMerOrders(params);
        }
        return orderMapper.queryList(params);
    }

    /**
     * 订单详情查询
     *
     * @param orderId
     * @return 订单详情
     */
    @Override
    public OrderVo findOrder(String orderId) {
        Assert.isBlank(orderId, "订单号不能为空!");
        return orderMapper.queryObject(orderId);
    }

    /**
     * @param orderVo
     */
    @Override
    public void updateOrder(OrderVo orderVo) {
        // 更新优惠券状态
        orderMapper.update(orderVo);
        if (orderVo.getCoupon_id() != null && orderVo.getCoupon_id() > 0
                && OrderStatusEnum.NEW == OrderStatusEnum.parse(orderVo.getOrder_status())) {
            UserCouponVo uc = UserCouponVo.builder()
                    .id(orderVo.getCoupon_id())
                    .coupon_status(1)
                    .used_time(null)
                    .build();
            userCouponMapper.updateCouponStatus(uc);
        }
        if (orderVo.getRefundVo() != null && orderVo.getRefundVo().getId() == 0) {
            orderRefundMapper.save(orderVo.getRefundVo());
        } else if (orderVo.getRefundVo() != null && orderVo.getRefundVo().getId() > 0) {
            orderRefundMapper.update(orderVo.getRefundVo());
        }
    }

    /**
     * @param newOrder
     */
    @Transactional(rollbackFor = {RuntimeException.class, Exception.class})
    @Override
    public void updateStatus(OrderVo newOrder) {
        if (CollectionUtils.isNotEmpty(newOrder.getFlowList())) {
            // 更新订单流水
//                        capitalFlowMapper.save(newOrder.getFlowList().get(0));
            financeFowService.saveCapitalFlow(newOrder.getFlowList().get(0));
        }
        orderMapper.updateStatus(newOrder);
    }

    /**
     * 商户管理订单
     *
     * @param orderVo
     */
    @Override
    public void handleOrderByMer(OrderVo orderVo) {
        if (orderVo.getRefundVo() != null) {
            orderRefundMapper.update(orderVo.getRefundVo());
        }
        orderMapper.update(orderVo);
    }

    /**
     * @param ids
     * @return
     */
    @Override
    public List<OrderVo> queryByIds(List<String> ids) {
        return orderMapper.queryListByIds(ids);
    }

    /**
     * 查询待处理的数据列表
     *
     * @param params
     * @return
     */
    @Override
    public List<OrderVo> queryPendingDataByTask(Map params) {
        return orderMapper.queryPendingDataByTask(params);
    }

    /**
     * 根据订单ID更新状态
     *
     * @param orderIds
     */
    @Override
    public int updateByIds(List<String> orderIds) {
        if (CollectionUtils.isEmpty(orderIds)) {
            return 0;
        }
        return orderMapper.cancelBatch(orderIds);
    }

    /**
     * 批量更新
     *
     * @param
     */
    @Override
    public int batchUpdate(List<OrderVo> orders) {
        if (CollectionUtils.isEmpty(orders)) {
            return 0;
        }
        return orderMapper.batchUpdate(orders);
    }

    private List<UserCouponVo> getUserCouponVos(String userCouponId, BigDecimal goodsTotalPrice, LoginUserVo loginUser) {
        List<UserCouponVo> userCouponList = Lists.newArrayList();
        if (StringUtils.isNotEmpty(userCouponId)) {
            // 优惠券
            String[] userCouponIds = userCouponId.split(",");
            List couponIds = Arrays.asList(userCouponIds).stream().filter(item -> Integer.parseInt(item) > 0).collect(Collectors.toList());
            if (CollectionUtils.isEmpty(couponIds)) {
                return userCouponList;
            }
            Map paramMap = Maps.newHashMap();
            paramMap.put("user_id", loginUser.getUserId());
            // 可用
            paramMap.put("coupon_status", CouponStatusEnum.COUPON_AVAILABLE.getCode());
            paramMap.put("couponIds", couponIds);
            paramMap.put("selected", true);
            // 商品总额
            paramMap.put("goodsTotalPrice", goodsTotalPrice);
            // 校验用户选择的优惠券
            userCouponList = userCouponMapper.queryList(paramMap);
        }
        return userCouponList;
    }
}
