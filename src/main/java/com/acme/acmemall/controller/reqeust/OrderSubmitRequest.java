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

    // 发票抬头ID
    private long headerId;

    // 用户已选择的优惠券
    private String userCouponId;

    // 提交方式cart-购物车
    private String type;

    // 实付金额=订单金额+运费-优惠券
    private String actualPrice;

    // 订单金额=商品总额-优惠
    private BigDecimal orderTotalPrice;

    // 商品金额
    private BigDecimal goodsTotalPrice;

    // 购物车明细
    private String cartIds;

    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer("OrderSubmitRequest{");
        sb.append("addressId=").append(addressId);
        sb.append(", headerId=").append(headerId);
        sb.append(", userCouponId='").append(userCouponId).append('\'');
        sb.append(", type='").append(type).append('\'');
        sb.append(", actualPrice='").append(actualPrice).append('\'');
        sb.append(", orderTotalPrice=").append(orderTotalPrice);
        sb.append(", goodsTotalPrice=").append(goodsTotalPrice);
        sb.append(", cartIds='").append(cartIds).append('\'');
        sb.append('}');
        return sb.toString();
    }

    public void check(){
        if (addressId == 0) {
            new ApiCusException("非有效收件人地址");
        }
    }
}
