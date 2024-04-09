package com.acme.acmemall.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * @description:
 * @author: ihpangzi
 * @time: 2023/2/13 17:38
 */
@Data
public class ProductVo implements Serializable {
    //主键
    private Long id;
    //商品Id
    private Long goods_id;
    //产品Id
    private Long product_id;
    //商品规格ids
    private String goods_specification_ids;
    // 规格名称
    private String goods_specifition_name_value;

    //商品序列号
    private String goods_sn;
    //商品库存
    private Long goods_number = 0L;
    //零售价格
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "##0.00")
    private BigDecimal market_price;
    //市场价
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "##0.00")
    private BigDecimal retail_price = BigDecimal.ZERO;
    //商品名称
    private String goods_name;
    //商品图片
    private String list_pic_url;
    //商户id
    private Long merchant_id;
    //活动价格
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "##0.00")
    private BigDecimal group_price;

    // 规格图
    private String spec_pic_url;

    private String specValue;
    private Integer specTypeId;

    public BigDecimal getMarket_price() {
        return market_price == null ? BigDecimal.ZERO : market_price.setScale(2, RoundingMode.HALF_UP);
    }

    public BigDecimal getRetail_price() {
        return retail_price == null ? BigDecimal.ZERO : retail_price.setScale(2, RoundingMode.HALF_UP);
    }

    public BigDecimal getGroup_price() {
        return group_price == null ? BigDecimal.ZERO : group_price.setScale(2, RoundingMode.HALF_UP);
    }

    public String getCheckedKey() {
        return goods_id + specValue;
    }

    public boolean verifyInventory(Integer goods_number) {
        return this.goods_number > goods_number;
    }
}
