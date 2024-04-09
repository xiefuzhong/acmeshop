package com.acme.acmemall.model;

import com.fasterxml.jackson.annotation.JsonFormat;
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

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "##0.00")
    public BigDecimal freightPrice;//运费

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "##0.00")
    public BigDecimal orderTotalPrice;//订单总金额

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "##0.00")
    public BigDecimal actualPrice;//实际支付金额

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "##0.00")
    public BigDecimal couponPrice;
    public List<CouponVo> userCouponList;//用户可用优惠券列表

    // 商品ID集合
    public List<Long> goodsIds;

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
