/*
 * Copyright (c) 2023. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */

package com.acme.acmemall.controller;

import com.acme.acmemall.annotation.LoginUser;
import com.acme.acmemall.dto.CouponInfoVo;
import com.acme.acmemall.model.CouponVo;
import com.acme.acmemall.model.LoginUserVo;
import com.acme.acmemall.model.ShopCartVo;
import com.acme.acmemall.service.ICouponService;
import com.acme.acmemall.service.IShopCartService;
import com.acme.acmemall.utils.StringUtils;
import com.google.common.collect.Maps;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @description:购物车功能
 * @author: ihpangzi
 * @time: 2023/2/13 11:20
 */
@Api(tags = "购物车")
@RestController
@RequestMapping("/api/cart")
public class ShopCartController extends ApiBase {

    @Autowired
    private IShopCartService cartService;

    @Autowired
    private ICouponService couponService;

    private static final BigDecimal ZERO = BigDecimal.ZERO;

    /**
     * 获取购物车信息，所有对购物车的增删改操作，都要重新返回购物车的信息
     */
    @ApiOperation(value = "获取购物车信息")
    @GetMapping("index")
    public Object index(@LoginUser LoginUserVo loginUser) {
        return toResponsSuccess(getCart(loginUser));
    }

    /**
     * 获取购物车中的数据
     */
    @ApiOperation(value = "获取购物车中的数据")
    @GetMapping("getCart")
    public Object getCart(@LoginUser LoginUserVo loginUser) {
        Map<String, Object> resultObj = Maps.newHashMap();
        //查询列表数据
        Map param = Maps.newHashMap();
        param.put("user_id", loginUser.getUserId());
        logger.info("user_id>>"+loginUser.getUserId());
        List<ShopCartVo> cartList = cartService.queryCartList(param);
        //获取购物车统计信息
        Integer goodsCount = 0; // 商品数量
        BigDecimal goodsAmount = BigDecimal.ZERO; // 商品金额
        Integer checkedGoodsCount = 0; // 校验数量
        BigDecimal checkedGoodsAmount = BigDecimal.ZERO; // 校验金额
        for (ShopCartVo cartItem : cartList) {
            goodsCount += cartItem.getNumber();
            goodsAmount = goodsAmount.add(cartItem.getAmount());
            if (null != cartItem.getChecked() && 1 == cartItem.getChecked()) {
                checkedGoodsCount += cartItem.getNumber();
                checkedGoodsAmount = checkedGoodsAmount.add(cartItem.getAmount());
            }
        }
        // 获取优惠信息提示
        Map couponParam = Maps.newHashMap();
        couponParam.put("enabled", true);
        Integer[] send_types = new Integer[]{0, 7};
        couponParam.put("send_types", send_types);
        List<CouponInfoVo> couponInfoList = new ArrayList();
        List<CouponVo> couponVos = couponService.queryCouponList(couponParam);
        if (null != couponVos && couponVos.size() > 0) {
            CouponInfoVo fullCutVo = new CouponInfoVo();
            BigDecimal fullCutDec = BigDecimal.ZERO;
            BigDecimal minAmount = new BigDecimal(100000);
            for (CouponVo couponVo : couponVos) {
                BigDecimal difDec = couponVo.getMin_goods_amount().subtract(checkedGoodsAmount).setScale(2, BigDecimal.ROUND_HALF_UP);
                if (couponVo.getSend_type() == 0 && difDec.doubleValue() > 0.0
                        && minAmount.compareTo(couponVo.getMin_goods_amount()) > 0) {
                    fullCutDec = couponVo.getType_money();
                    minAmount = couponVo.getMin_goods_amount();
                    fullCutVo.setType(1);
                    fullCutVo.setMsg(couponVo.getName() + "，还差" + difDec + "元");
                } else if (couponVo.getSend_type() == 0 && difDec.doubleValue() < 0.0 && fullCutDec.compareTo(couponVo.getType_money()) < 0) {
                    fullCutDec = couponVo.getType_money();
                    fullCutVo.setType(0);
                    fullCutVo.setMsg("可使用满减券" + couponVo.getName());
                }
                if (couponVo.getSend_type() == 7 && difDec.doubleValue() > 0.0) {
                    CouponInfoVo cpVo = new CouponInfoVo();
                    cpVo.setMsg("满￥" + couponVo.getMin_amount() + "元免配送费，还差" + difDec + "元");
                    cpVo.setType(1);
                    couponInfoList.add(cpVo);
                } else if (couponVo.getSend_type() == 7) {
                    CouponInfoVo cpVo = new CouponInfoVo();
                    cpVo.setMsg("满￥" + couponVo.getMin_amount() + "元免配送费");
                    couponInfoList.add(cpVo);
                }
            }
            if (!StringUtils.isNullOrEmpty(fullCutVo.getMsg())) {
                couponInfoList.add(fullCutVo);
            }
        }
        resultObj.put("couponInfoList", couponInfoList);
        resultObj.put("cartList", cartList);
        //
        Map<String, Object> cartTotal = new HashMap();
        cartTotal.put("goodsCount", goodsCount);
        cartTotal.put("goodsAmount", goodsAmount);
        cartTotal.put("checkedGoodsCount", checkedGoodsCount);
        cartTotal.put("checkedGoodsAmount", checkedGoodsAmount);
        //
        resultObj.put("cartTotal", cartTotal);
        return resultObj;
    }

}
