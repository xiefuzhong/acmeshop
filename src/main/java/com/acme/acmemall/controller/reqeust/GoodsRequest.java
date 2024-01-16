package com.acme.acmemall.controller.reqeust;

import lombok.Data;

import java.io.Serializable;

/**
 * @description:
 * @author: ihpangzi
 * @time: 2024/1/16 18:38
 */
@Data
public class GoodsRequest implements Serializable {
    private int brand_id; // 品牌 ID
    private double extra_price; // 快递费
    private String name; //商品名称
    private String goods_brief; // 简介描述
    private String keyword; // 关键字
    private int category_id; // 分类ID
    private String goods_unit; // 单位
    private int status; // 状态
    //    private long primary_pic_id;
//    private long list_pic_id;
    private String primary_pic_url; //  主图链接
    private String list_pic_url; // 列表缩略图链接
    //    private List<ImgAttachment> images;
    private long merchantId;    // 商户ID
}
