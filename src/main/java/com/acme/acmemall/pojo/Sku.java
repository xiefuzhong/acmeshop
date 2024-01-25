package com.acme.acmemall.pojo;

import lombok.Data;

/**
 * @description:
 * @author: ihpangzi
 * @time: 2024/1/25 15:23
 */
@Data
public class Sku {
    private int stock;
    private int cost_price;
    private String pic_url;
    private int price;
    private int ot_price;
    private String goodsNum;
    private String specValue;
    private int specTypeId;
    private int sortId;
    private int specId = 0;

    public String getContainsKey(long goods_id) {
        StringBuffer buffer = new StringBuffer();
        buffer.append(goods_id).append(specTypeId).append(specValue);
        return buffer.toString();
    }
}
