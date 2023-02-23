package com.acme.acmemall.controller.reqeust;

import com.acme.acmemall.exception.ApiCusException;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * @description:
 * @author: ihpangzi
 * @time: 2023/2/23 9:22
 */
@Data
public class OrderSubmitRequest implements Serializable {
    // 收件人地址ID
    private long addressId;

    // 用户已选择的优惠券
    private String userCouponId;

    // 提交方式cart-购物车
    private String type;

    // 实付金额=订单金额+运费
    private String actualPrice;

    // 订单金额=商品总额-优惠
    private BigDecimal orderTotalPrice;

    // 商品金额
    private BigDecimal goodsTotalPrice;

    // 购物车明细
    private String cartIds;

    public void check(){
        if (addressId == 0) {
            new ApiCusException("非有效收件人地址");
        }
    }
}
