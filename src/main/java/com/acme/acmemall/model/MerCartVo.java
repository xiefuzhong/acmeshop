package com.acme.acmemall.model;

import lombok.Data;
import org.apache.commons.collections.CollectionUtils;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

@Data
public class MerCartVo implements Serializable {
    private static final long serialVersionUID = 1L;
    public  Long merchantId;
    public String merchantName;
    public List<ShopCartVo> cartVoList;
    public BigDecimal freightPrice;//运费
    public BigDecimal orderTotalPrice;//订单总金额
    public BigDecimal actualPrice;//实际支付金额
    public List<CouponVo> userCouponList;//用户可用优惠券列表

    /**
     * 实际支付金额计算
     * @return 实际支付金额
     */
    public BigDecimal getActualPrice() {
        if (CollectionUtils.isEmpty(cartVoList)) {
            return BigDecimal.ZERO;
        }

        return actualPrice;
    }
}
