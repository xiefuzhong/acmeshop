package com.acme.acmemall.service.impl;

import com.acme.acmemall.common.ResultMap;
import com.acme.acmemall.controller.reqeust.OrderSubmitRequest;
import com.acme.acmemall.dao.*;
import com.acme.acmemall.exception.ResultCodeEnum;
import com.acme.acmemall.factory.OrderFactory;
import com.acme.acmemall.model.*;
import com.acme.acmemall.service.IOrderService;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @description:
 * @author: ihpangzi
 * @time: 2023/2/17 15:58
 */
@Service
public class OrderService implements IOrderService {
    private final UserCouponMapper userCouponMapper;
    private final AddressMapper addressMapper;
    private final ShopCartMapper cartMapper;

    private final OrderMapper orderMapper;

    private final OrderGoodsMapper orderItemMapper;

    protected Logger logger = Logger.getLogger(getClass());

    @Autowired
    public OrderService(UserCouponMapper userCouponMapper,
                        AddressMapper addressMapper,
                        ShopCartMapper cartMapper,
                        OrderMapper orderMapper,
                        OrderGoodsMapper orderItemMapper) {
        this.userCouponMapper = userCouponMapper;
        this.addressMapper = addressMapper;
        this.cartMapper = cartMapper;
        this.orderMapper = orderMapper;
        this.orderItemMapper = orderItemMapper;
    }

    /**
     * 订单提交
     *
     * @param request   请求参数
     * @param loginUser 登录用户信息
     * @return 订单提交结果
     */
    @Transactional
    @Override
    public ResultMap submit(OrderSubmitRequest request, LoginUserVo loginUser) {
        // 提交方式:cart-购物车提交 2-直接购买;其他-团购购买
        if (!StringUtils.equals("cart", request.getType())) {
            return ResultMap.error(101, "请先添加到购物车,暂不支持其他方式");
        }
        AddressVo addressVo = addressMapper.queryObject(request.getAddressId());
        if (addressVo == null || addressVo.getUserId() != loginUser.getUserId()) {
            return ResultMap.error(102, "请选择联系地址");
        }
        // 用户选择的优惠券信息
        List<UserCouponVo> userCouponList = Lists.newArrayList();
        if (StringUtils.isNotEmpty(request.getUserCouponId())) {
            // 优惠券
            String[] userCouponIds = request.getUserCouponId().split(",");
            Map paramMap = Maps.newHashMap();
            paramMap.put("user_id", loginUser.getUserId());
            paramMap.put("coupon_status", 1); // 可用
            paramMap.put("couponIds", Lists.newArrayList(userCouponIds));
            paramMap.put("selected", true);
            // 商品总额
            paramMap.put("goodsTotalPrice", request.getGoodsTotalPrice());
            // 校验用户选择的优惠券
            userCouponList = userCouponMapper.queryList(paramMap);
            if (CollectionUtils.isEmpty(userCouponList) || userCouponIds.length != userCouponList.size()) {
                return ResultMap.response(ResultCodeEnum.FAILED);
            }
        }

        // 购物车明细校验
        if (StringUtils.isEmpty(request.getCartIds())) {
            return ResultMap.response(ResultCodeEnum.FAILED);
        }
        // 查询用户已选择的购物车中信息，用来重新计算校验
        String[] cartIds = request.getCartIds().split(",");
        Map paramMap = Maps.newHashMap();
        paramMap.put("userId", loginUser.getUserId());

        paramMap.put("checked", 1);
        paramMap.put("cartIds", Lists.newArrayList(cartIds));
        List<ShopCartVo> cartList = cartMapper.queryList(paramMap);

        OrderVo orderInfo = OrderFactory.buildCartOrder(loginUser.getUserId());
        orderInfo.submit(userCouponList, cartList, addressVo);
        orderInfo.check();

        // 保存order以及明细表
        orderMapper.save(orderInfo);
        orderItemMapper.saveBatch(orderInfo.getItems());
        // 释放优惠券信息
        UserCouponVo uc = UserCouponVo.builder().coupon_id(orderInfo.getCoupon_id())
                .coupon_status(2)
                .used_time(new Date())
                .build();
        userCouponMapper.updateCouponStatus(uc);
        return ResultMap.response(ResultCodeEnum.SUCCESS, orderInfo);
    }
}
