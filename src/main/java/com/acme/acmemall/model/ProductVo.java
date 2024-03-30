package com.acme.acmemall.model;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

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
    private long goods_number;
    //零售价格
    private BigDecimal market_price;
    //时长价
    private BigDecimal retail_price;
    //商品名称
    private String goods_name;
    //商品图片
    private String list_pic_url;
    //商户id
    private Long merchant_id;
    //活动价格
    private BigDecimal group_price;

    // 规格图
    private String spec_pic_url;

    private String specValue;
    private Integer specTypeId;

    public String getCheckedKey() {
        StringBuffer buffer = new StringBuffer();
        buffer.append(goods_id).append(specValue);
        return buffer.toString();
    }
    public boolean verifyInventory(Integer goods_number) {
        return this.goods_number > goods_number;
    }
}
