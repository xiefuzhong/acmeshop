package com.acme.acmemall.model;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

/**
 * @description:品牌
 * @author: ihpangzi
 * @time: 2023/2/13 17:36
 */
@Data
public class BrandVo implements Serializable {
    //主键
    private Integer id;
    //品牌名称
    private String name;
    //图片
    private String list_pic_url;
    //描述
    private String simple_desc;
    //图片
    private String pic_url;
    //排序
    private Integer sort_order;
    //显示
    private Integer is_show;
    //
    private BigDecimal floor_price;
    //app显示图片
    private String app_list_pic_url;
    //新品牌
    private Integer is_new;
    //图片
    private String new_pic_url;
    //排序
    private Integer new_sort_order;
    //折扣率
    private Integer  max_discount;
    //总折扣金额
    private BigDecimal sum_discount;
    private String logo;
    private List<GoodsVo> goods;
    //商户id
    public  Long merchantId;
}
