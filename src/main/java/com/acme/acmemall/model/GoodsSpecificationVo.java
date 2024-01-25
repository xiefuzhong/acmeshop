package com.acme.acmemall.model;

import lombok.Data;

import java.io.Serializable;

/**
 * @description: 商品规格
 * @author: ihpangzi
 * @time: 2023/2/13 17:33
 */
@Data
public class GoodsSpecificationVo implements Serializable {
    //主键
    private Integer id;
    //商品
    private Integer goods_id;
    //规范Id
    private Integer specification_id;
    //规范说明
    private String value;
    private String name;
    //规范图片
    private String pic_url;

    // 1-启用 0-停用
    private Integer enabled;


    public String getContainsKey() {
        StringBuffer buffer = new StringBuffer();
        buffer.append(goods_id).append(value);
        return buffer.toString();
    }
}
