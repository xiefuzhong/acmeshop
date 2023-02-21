package com.acme.acmemall.service.impl;

import com.acme.acmemall.common.ResultMap;
import com.acme.acmemall.model.*;
import com.acme.acmemall.service.IAddressService;
import com.acme.acmemall.service.ICouponService;
import com.acme.acmemall.service.IOrderService;
import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Maps;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @description:
 * @author: ihpangzi
 * @time: 2023/2/17 15:58
 */
@Service
public class OrderService implements IOrderService {
    protected Logger logger = Logger.getLogger(getClass());

    private final ICouponService couponService;

    private final IAddressService addressService;

    @Autowired
    public OrderService(ICouponService couponService,IAddressService addressService) {
        this.couponService = couponService;
        this.addressService = addressService;
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
    public Map submit(JSONObject request, LoginUserVo loginUser) {
        logger.info("order.submit--> " + request.toString());
//        Integer addressId = request.getInteger("addressId");
//        if (addressId == null || addressId.intValue() == 0) {
//            return ResultMap.error(103,"请先选择或添加收件地址!");
//        }
        // 购物车商品ID
        String[] cartIds = request.getString("cartIds").split("_");

        // 校验是否选择
        AddressVo checkedAddress = request.getObject("checkedAddress", AddressVo.class);
        if (checkedAddress == null) {
            return ResultMap.error(103,"请先选择或添加收件地址!");
        }
        AddressVo addressVo = addressService.queryObject(checkedAddress.getId().intValue());
        if (addressVo == null) {
            return ResultMap.error(103,"请先选择或添加收件地址!");
        }

        // 已选择的优惠券，需要校验优惠券是否被使用
        String couponIds = request.getString("couponId");
        Map param = Maps.newHashMap();
        param.put("user_id", loginUser.getUserId());
        // 可用
        param.put("coupon_status", 1);
//        param.put("merchantId", loginUser.get);
        String[] couponList = couponIds.split(",");
        param.put("couponIds", couponList);
        List<UserCouponVo> userCouponVos = couponService.queryUserCouponList(param);
        if (userCouponVos == null) {
            return ResultMap.error(702,"非有效优惠券,请从正规渠道获取!");
        }
        // cart-购物车提交 2-直接购买;其他-团购购买
        String type = request.getString("type");//提交方式
        if (!StringUtils.equals("cart",type)){
            return ResultMap.error(102,"请先添加到购物车,暂不支持");
        }

        // MerCartVo
        String checkedGoodsList = request.getString("checkedGoodsList");
        List<MerCartVo> merCartVoList = JSONObject.parseArray(checkedGoodsList, MerCartVo.class);
        // 按商户分组商品列表，用来校验优惠券
        Map<Long, List<MerCartVo>> merCartMap = merCartVoList.stream().collect(Collectors.groupingBy(MerCartVo::getMerchantId));
        // 校验优惠券有效性
        if (StringUtils.isNotBlank(couponIds)) {

        }
        // payType 1：团购操作，2：秒杀，不传为普通购买
        String payType = request.getString("payType");

        OrderVo orderInfo = new OrderVo();
        // 保存order以及明细表
        Map<String, OrderVo> orderInfoMap = Maps.newHashMap();
        return ResultMap.ok();
    }
}
