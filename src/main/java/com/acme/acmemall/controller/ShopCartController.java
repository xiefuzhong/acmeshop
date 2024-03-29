/*
 * Copyright (c) 2023. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */

package com.acme.acmemall.controller;

import com.acme.acmemall.annotation.LoginUser;
import com.acme.acmemall.common.ResultMap;
import com.acme.acmemall.dto.CouponInfoVo;
import com.acme.acmemall.model.*;
import com.acme.acmemall.service.*;
import com.acme.acmemall.utils.StringUtils;
import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @description:购物车功能
 * @author: ihpangzi
 * @time: 2023/2/13 11:20
 */
@Api(tags = "购物车")
@RestController
@RequestMapping("/api/cart")
public class ShopCartController extends ApiBase {

    private static final BigDecimal ZERO = BigDecimal.ZERO;
    @Autowired
    IShopCartService cartService;
    @Autowired
    ICouponService couponService;

    @Autowired
    IGoodsSpecService goodsSpecService;

    @Autowired
    IGoodsService goodsService;

    @Autowired
    IProductService productService;

    @Autowired
    IAddressService addressService;

    @Autowired
    IInvoiceTitleService invoiceTitleService;


    /**
     * 获取购物车信息，所有对购物车的增删改操作，都要重新返回购物车的信息
     */
    @ApiOperation(value = "获取购物车信息")
    @GetMapping("index")
    public Object index(@LoginUser LoginUserVo loginUser) {
//        logger.info("cart.index.loginuser>>" + JSONObject.toJSON(loginUser));
        return toResponsSuccess(getCart(loginUser));
    }

    /**
     * 获取购物车中的数据
     */
    @ApiOperation(value = "获取购物车中的数据")
    @GetMapping("getCart")
    public Object getCart(@LoginUser LoginUserVo loginUser) {
        logger.info("获取购物车中的数据");
        Map<String, Object> resultObj = Maps.newHashMap();
        //查询列表数据
        Map param = Maps.newHashMap();
        param.put("user_id", loginUser.getUserId());
        logger.info("user_id>>" + loginUser.getUserId());
        List<ShopCartVo> cartList = cartService.queryCartList(param);
        logger.info("cart.list.size =" + (cartList.isEmpty() ? 0 : cartList.size()));
        // 获取购物车统计信息
        Integer goodsCount = 0; // 商品数量
        // 商品金额
        BigDecimal goodsAmount = BigDecimal.ZERO;
        // 校验数量
        Integer checkedGoodsCount = 0;
        // 校验金额
        BigDecimal checkedGoodsAmount = BigDecimal.ZERO;
        for (ShopCartVo cartItem : cartList) {
            goodsCount += cartItem.getNumber();
            goodsAmount = goodsAmount.add(cartItem.getGoodsTotalAmount());
            if (null != cartItem.getChecked() && 1 == cartItem.getChecked()) {
                checkedGoodsCount += cartItem.getNumber();
                checkedGoodsAmount = checkedGoodsAmount.add(cartItem.getGoodsTotalAmount());
            }
        }
        // 获取优惠信息提示
        Map couponParam = Maps.newHashMap();
        couponParam.put("enabled", true);
        Integer[] send_types = new Integer[]{0, 7};
        couponParam.put("send_types", send_types);
        List<CouponInfoVo> couponInfoList = Lists.newArrayList();
        List<CouponVo> couponVos = couponService.queryCouponList(couponParam);
        if (null != couponVos && couponVos.size() > 0) {
            CouponInfoVo fullCutVo = new CouponInfoVo();
            BigDecimal fullCutDec = BigDecimal.ZERO;
            BigDecimal minAmount = new BigDecimal(100000);
            for (CouponVo couponVo : couponVos) {
                BigDecimal difDec = couponVo.getMin_goods_amount().subtract(checkedGoodsAmount).setScale(2, RoundingMode.HALF_UP);
                if (couponVo.getSend_type() == 0 && difDec.doubleValue() > 0.0 && minAmount.compareTo(couponVo.getMin_goods_amount()) > 0) {
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
        Map<String, Object> cartTotal = Maps.newHashMap();
        cartTotal.put("goodsCount", goodsCount);
        cartTotal.put("goodsAmount", goodsAmount);
        cartTotal.put("checkedGoodsCount", checkedGoodsCount);
        cartTotal.put("checkedGoodsAmount", checkedGoodsAmount);
        //
        resultObj.put("cartTotal", cartTotal);
        return resultObj;
    }

    /**
     * 添加商品到购物车
     */
    @ApiOperation(value = "添加商品到购物车")
    @PostMapping("add")
    public Object add(@LoginUser LoginUserVo loginUser) {
        JSONObject jsonParam = getJsonRequest();
        Integer goodsId = jsonParam.getInteger("goodsId");
        Integer productId = jsonParam.getInteger("productId");
        Integer number = jsonParam.getInteger("number");
        String type = jsonParam.getString("ntype");
        //判断商品是否可以购买
        GoodsVo goodsInfo = goodsService.queryObject(goodsId);
        if (null == goodsInfo || goodsInfo.checkOff()) {
            return ResultMap.error(400, "商品已下架");
        }
        //取得规格的信息,判断规格库存
        ProductVo productInfo = productService.queryObject(productId);
        if (null == productInfo || !productInfo.verifyInventory(number)) {
            return ResultMap.error(400, "库存不足");
        }
        // 清空购物车
        cartService.deleteCartByUserId(loginUser.getUserId());

        //判断购物车中是否存在此规格商品
        Map cartParam = Maps.newHashMap();
        cartParam.put("goods_id", goodsId);
        cartParam.put("product_id", productId);
        cartParam.put("user_id", loginUser.getUserId());
        List<ShopCartVo> cartInfoList = cartService.queryCartList(cartParam);
        ShopCartVo cartInfo = CollectionUtils.isNotEmpty(cartInfoList) ? cartInfoList.get(0) : null;
        if (null == cartInfo) {
            //添加操作
            //添加规格名和值
            String sepcifitionValue = null;
            if (StringUtils.isNotEmpty(productInfo.getGoods_specification_ids())) {
                Map specificationParam = Maps.newHashMap();
                List<String> ids = Arrays.stream(productInfo.getGoods_specification_ids().split("_")).collect(Collectors.toList());
                specificationParam.put("ids", ids);
                specificationParam.put("goods_id", goodsId);
                List<GoodsSpecificationVo> specificationEntities = goodsSpecService.queryGoodsSpecList(specificationParam);
                sepcifitionValue = specificationEntities.stream().map(GoodsSpecificationVo::getValue).collect(Collectors.joining(";"));
            }
            cartInfo = ShopCartVo.builder().user_id(loginUser.getUserId()).build();

            if (null != sepcifitionValue) {
                productInfo.setGoods_specifition_name_value(sepcifitionValue);
            }
            cartInfo.addToCart(goodsInfo, productInfo, number);
            cartService.save(cartInfo);
        } else {
            //如果已经存在购物车中，则数量增加
            if (!productInfo.verifyInventory(number + cartInfo.getNumber())) {
                return ResultMap.error(400, "库存不足");
            }
            // 直接购买无购物车替换数量
            cartInfo.addToCart(null, null, number);
//            cartInfo.addToCart(null, null, cartInfo.getNumber() + number);
            cartService.update(cartInfo);
        }
        return toResponsSuccess(getCart(loginUser));
    }

    /**
     * 更新指定的购物车信息
     */
    @ApiOperation(value = "更新指定的购物车信息")
    @PostMapping("update")
    public Object update(@LoginUser LoginUserVo loginUser) {
        JSONObject jsonParam = getJsonRequest();
        Integer goodsId = jsonParam.getInteger("goodsId");
        Integer productId = jsonParam.getInteger("productId");
        Integer number = jsonParam.getInteger("number");
        Integer id = jsonParam.getInteger("id");
        //取得规格的信息,判断规格库存
        ProductVo productInfo = productService.queryObject(productId);
        if (null == productInfo || productInfo.getGoods_number() < number) {
            return this.toResponsObject(400, "库存不足", "");
        }
        //判断是否已经存在product_id购物车商品
        ShopCartVo cartInfo = cartService.queryObject(id);
        //只是更新number
        if (cartInfo.getProduct_id().equals(productId)) {
            cartInfo.addToCart(null, null, number);
            cartService.update(cartInfo);
            return toResponsSuccess(getCart(loginUser));
        }

        Map cartParam = Maps.newHashMap();
        cartParam.put("goodsId", goodsId);
        cartParam.put("productId", productId);
        List<ShopCartVo> cartInfoList = cartService.queryCartList(cartParam);
        ShopCartVo newcartInfo = null != cartInfoList && cartInfoList.size() > 0 ? cartInfoList.get(0) : null;
        if (null == newcartInfo) {
            //添加操作
            //添加规格名和值
            this.setSepcifitionValue(goodsId, productInfo);
            cartInfo.addToCart(null, productInfo, number);
            cartService.update(cartInfo);
        } else {
            //合并购物车已有的product信息，删除已有的数据
            Integer newNumber = number + newcartInfo.getNumber();
            if (null == productInfo || productInfo.getGoods_number() < newNumber) {
                return this.toResponsObject(400, "库存不足", "");
            }
            cartService.delete(newcartInfo.getId());
            //添加规格名和值
            this.setSepcifitionValue(goodsId, productInfo);
            cartInfo.addToCart(null, productInfo, number);
            cartService.update(cartInfo);
        }
        return toResponsSuccess(getCart(loginUser));
    }

    private void setSepcifitionValue(Integer goodsId, ProductVo productInfo) {
        String sepcifitionValue = null;
        if (null != productInfo.getGoods_specification_ids()) {
            Map specificationParam = Maps.newHashMap();
            specificationParam.put("ids", productInfo.getGoods_specification_ids());
            specificationParam.put("goodsId", goodsId);
            List<GoodsSpecificationVo> specificationEntities = goodsSpecService.queryGoodsSpecList(specificationParam);
            sepcifitionValue = specificationEntities.stream().map(GoodsSpecificationVo::getValue).collect(Collectors.joining(";"));
        }
        if (StringUtils.isNotEmpty(sepcifitionValue)) {
            productInfo.setGoods_specifition_name_value(sepcifitionValue);
        }
    }

    /**
     * 删除指定的购物车信息
     */
    @ApiOperation(value = "删除商品")
    @PostMapping("delete")
    public Object delete(@LoginUser LoginUserVo loginUser) {
        Long userId = loginUser.getUserId();

        JSONObject jsonObject = getJsonRequest();
        String productIds = jsonObject.getString("productIds");

        if (StringUtils.isNullOrEmpty(productIds)) {
            return toResponsFail("删除出错");
        }
        String[] productIdsArray = productIds.split(",");
        cartService.deleteByUserAndProductIds(userId, productIdsArray);

        return toResponsSuccess(getCart(loginUser));
    }

    /**
     * 获取购物车商品的总件件数
     *
     * @param loginUser
     * @return
     */
    @ApiOperation(value = "获取购物车商品的总件件数")
    @GetMapping("goodscount")
    public Object goodscount(@LoginUser LoginUserVo loginUser) {
        if (null == loginUser || null == loginUser.getUserId()) {
            return toResponsFail("未登录");
        }
        Map<String, Object> resultObj = Maps.newHashMap();
        //查询列表数据
        Map param = Maps.newHashMap();
        param.put("user_id", loginUser.getUserId());
        List<ShopCartVo> cartList = cartService.queryCartList(param);
        //获取购物车统计信息
        Integer goodsCount = 0;
        for (ShopCartVo cartItem : cartList) {
            goodsCount += cartItem.getNumber();
        }
        resultObj.put("cartList", cartList);
        //
        Map<String, Object> cartTotal = Maps.newHashMap();
        cartTotal.put("goodsCount", goodsCount);
        //
        resultObj.put("cartTotal", cartTotal);
        return toResponsSuccess(resultObj);
    }

    /**
     * 下单前信息确认：订单提交前的检验和填写相关订单信息
     * activityType 1 直接购买  2 团购购买
     */
    @ApiOperation(value = "订单提交前的检验和填写相关订单信息")
    @GetMapping("checkout")
    public Object checkout(@LoginUser LoginUserVo loginUser,
                           Long couponId,
                           @RequestParam(defaultValue = "cart") String type,
                           Integer addressId,
                           Integer headerId,
                           String activityType) {
        //activityType="2";
        Map<String, Object> resultObj = Maps.newHashMap();

        //根据收货地址计算运费,当前取商品的附加值做运费(预先设定或可编辑根据实际咨询获得)
        BigDecimal freightPrice = BigDecimal.ZERO;

        //订单总金额
        BigDecimal goodsTotalPrice = BigDecimal.ZERO;

        //默认收货地址
        AddressVo checkedAddress = null;
        if (StringUtils.isNullOrEmpty(addressId) || addressId == 0) {
            checkedAddress = addressService.queryDefaultAddress(loginUser.getUserId());//设置默认地址
        } else {
            checkedAddress = addressService.queryObject(addressId);
        }
        resultObj.put("checkedAddress", checkedAddress);

        // 获得发票抬头@todo
        InvoiceTitleVo checkedInvoiceTitle = null;
        if (StringUtils.isNullOrEmpty(headerId) || headerId == 0) {
            checkedInvoiceTitle = invoiceTitleService.queryDefaultByUserId(loginUser.getUserId());
        } else {
            checkedInvoiceTitle = invoiceTitleService.queryObject(headerId);
        }
        resultObj.put("checkedInvoiceTitle", checkedInvoiceTitle);

        // * 获取要购买的商品和总价
        ArrayList checkedGoodsList = Lists.newArrayList();
        CouponVo couponVo = null;
        if (couponId != null && couponId > 0) {
            couponVo = couponService.queryObject(couponId);
        }
        List<MerCartVo> merCartVoList = Lists.newArrayList();
        // 购物车下单
        if (type.equals("cart")) {
            //查询用户购物车信息
            List<MerCartVo> merCartVos = cartService.queryMerCartList(loginUser.getUserId());
            for (MerCartVo merCartVo : merCartVos) {
                freightPrice = freightPrice.add(merCartVo.getFreightPrice());
                goodsTotalPrice = goodsTotalPrice.add(merCartVo.getOrderTotalPrice());
                // 获取优惠金额
                // 订单的总价
                BigDecimal orderTotalPrice = goodsTotalPrice.add(freightPrice);
                BigDecimal couponPrice = getCouponPrice(couponVo, orderTotalPrice);
                BigDecimal actualPrice = orderTotalPrice.subtract(couponPrice);  //减去其它支付的金额后，要实际支付的金额
                merCartVo.setCouponPrice(couponPrice);
                merCartVo.setActualPrice(actualPrice);
                Map map = Maps.newHashMap();
                map.put("user_id", loginUser.getUserId());
                map.put("merchantId", merCartVo.getMerchantId());
                map.put("goodsTotalPrice", merCartVo.getOrderTotalPrice());
                List<ShopCartVo> cartVoList = cartService.queryCheckedByUserIdAndMerId(map);
                List<Long> goodsIds = cartVoList.stream().map(ShopCartVo::getGoods_id).collect(Collectors.toList());
                merCartVo.setCartVoList(cartVoList);
                //获取用户可用优惠券列表
                List<CouponVo> validCouponVos = couponService.getValidUserCoupons(map);
                merCartVo.setUserCouponList(validCouponVos);
                merCartVoList.add(merCartVo);
            }
        } else { // 是直接购买的
            BuyGoodsVo goodsVO = new BuyGoodsVo();
            ProductVo productInfo = productService.queryObject(goodsVO.getProductId());
            GoodsVo goods = goodsService.queryObject(goodsVO.getGoodsId());
            //计算订单的费用
            //商品总价
            if (goods.getIs_secKill() == 3) {
                if ("2".equals(activityType)) {//团购购买
                    productInfo.setRetail_price(productInfo.getGroup_price());
                }
            }
            goodsTotalPrice = productInfo.getRetail_price().multiply(new BigDecimal(goodsVO.getNumber()));

            ShopCartVo cartVo = ShopCartVo.builder().goods_name(productInfo.getGoods_name()).number(goodsVO.getNumber()).retail_price(productInfo.getRetail_price()).list_pic_url(productInfo.getList_pic_url()).build();
            checkedGoodsList.add(cartVo);

            //计算运费

            if (goods.getExtra_price() != null) {
                freightPrice = freightPrice.add(goods.getExtra_price().multiply(new BigDecimal(cartVo.getNumber())));
            }
            MerCartVo merCartVo = new MerCartVo();
            merCartVo.setMerchantId(productInfo.getMerchant_id());
            String merchantName = cartService.queryMerchantName(merCartVo.getMerchantId());
            merCartVo.setMerchantName(merchantName);
            merCartVo.setCartVoList(checkedGoodsList);
            merCartVo.setOrderTotalPrice(goodsTotalPrice);
            merCartVo.setFreightPrice(freightPrice);
            merCartVo.setActualPrice(goodsTotalPrice.add(freightPrice));
            //获取优惠券
            Map map = Maps.newHashMap();
            map.put("user_id", loginUser.getUserId());
            map.put("merchantId", merCartVo.getMerchantId());
            map.put("goodsTotalPrice", merCartVo.getOrderTotalPrice());
            List<CouponVo> validCouponVos = couponService.getValidUserCoupons(map);
            merCartVo.setUserCouponList(validCouponVos);
            merCartVoList.add(merCartVo);
        }
        // 订单的总价
        BigDecimal orderTotalPrice = goodsTotalPrice.add(freightPrice);
        //获取可用的优惠券信息
        BigDecimal couponPrice = getCouponPrice(couponVo, orderTotalPrice);

        BigDecimal actualPrice = orderTotalPrice.subtract(couponPrice);  //减去其它支付的金额后，要实际支付的金额
        resultObj.put("freightPrice", freightPrice);
        resultObj.put("couponPrice", couponPrice);
        resultObj.put("checkedGoodsList", merCartVoList);
        resultObj.put("goodsTotalPrice", goodsTotalPrice);
        resultObj.put("orderTotalPrice", orderTotalPrice);
        resultObj.put("actualPrice", actualPrice);
        return toResponsSuccess(resultObj);
    }

    /**
     * 获取优惠券金额<br>
     * 1.满减-直接返回优惠券金额<br>
     * 2.折扣-计算折扣金额
     *
     * @param couponVo        优惠券信息
     * @param orderTotalPrice 订单金额
     * @return couponPrice
     */
    private static BigDecimal getCouponPrice(CouponVo couponVo, BigDecimal orderTotalPrice) {
        BigDecimal couponPrice = new BigDecimal("0.00");
        if (couponVo != null) {
            //  优惠券类型 1:满减 2:折扣
            if (couponVo.getType() == 1) {
                couponPrice = couponVo.getType_money();
            } else if (couponVo.getType() == 2) {
                couponPrice = orderTotalPrice.multiply(new BigDecimal(100).subtract(couponVo.getType_money())).divide(new BigDecimal(100));
            }
        }
        return couponPrice;
    }

    /**
     * 是否选择商品，如果已经选择，则取消选择，批量操作
     */
    @ApiOperation(value = "是否选择商品")
    @PostMapping("checked")
    public Object checked(@LoginUser LoginUserVo loginUser) {
        JSONObject request = getJsonRequest();
        String productIds = request.getString("productIds");
        Integer isChecked = request.getInteger("isChecked");
        if (StringUtils.isNullOrEmpty(productIds)) {
            return this.toResponsFail("删除出错");
        }
        String[] productIdArray = productIds.split(",");
        cartService.updateCheck(productIdArray, isChecked, loginUser.getUserId());
        return toResponsSuccess(getCart(loginUser));
    }

    /**
     * 直接购买：添加到购物车-跳转到确认订单页面
     *
     * @param loginUser
     * @return
     */
    @ApiOperation(value = "直接购买")
    @PostMapping("/buy")
    public ResultMap directPurchase(@LoginUser LoginUserVo loginUser) {
        JSONObject reqParam = super.getJsonRequest();
        Integer goodsId = reqParam.getInteger("goodsId");
        Integer productId = reqParam.getInteger("productId");
        Integer number = reqParam.getInteger("number");

        GoodsVo goodsInfo = goodsService.queryObject(goodsId);
        if (goodsInfo == null || goodsInfo.checkOff()) {
            return ResultMap.error(400, "商品已下架");
        }
        //取得规格的信息,判断规格库存
        ProductVo productInfo = productService.queryObject(productId);
        if (null == productInfo || !productInfo.verifyInventory(number)) {
            return ResultMap.error(400, "库存不足");
        }

        //判断购物车中是否存在此规格商品
        Map cartParam = Maps.newHashMap();
        cartParam.put("goods_id", goodsId);
        cartParam.put("product_id", productId);
        cartParam.put("user_id", loginUser.getUserId());
        List<ShopCartVo> cartInfoList = cartService.queryCartList(cartParam);
        ShopCartVo cartInfo = null;
        if (CollectionUtils.isNotEmpty(cartInfoList)) {
            cartInfo = cartInfoList.stream().findFirst().get();
        }
        if (cartInfo == null) {


        } else {
            //如果已经存在购物车中，则数量增加
            if (!productInfo.verifyInventory(number + cartInfo.getNumber())) {
                return ResultMap.error(400, "库存不足");
            }
            cartInfo.addToCart(null, null, cartInfo.getNumber() + number);
            cartService.update(cartInfo);
        }
        return ResultMap.ok();
    }

    private String[] getSpecificationIdsArray(String ids) {
        String[] idsArray = null;
        if (org.apache.commons.lang.StringUtils.isNotEmpty(ids)) {
            String[] tempArray = ids.split("_");
            if (null != tempArray && tempArray.length > 0) {
                idsArray = tempArray;
            }
        }
        return idsArray;
    }

}
