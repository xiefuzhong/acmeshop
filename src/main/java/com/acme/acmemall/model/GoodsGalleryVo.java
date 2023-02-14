package com.acme.acmemall.model;

import lombok.Data;

import java.io.Serializable;

/**
 * @description:商品列表
 * @author: ihpangzi
 * @time: 2023/2/13 18:17
 */
@Data
public class GoodsGalleryVo implements Serializable {
    //主键
    private Integer id;
    //商品id
    private Integer goods_id;
    //图片
    private String img_url;
    //描述
    private String img_desc;
    //排序
    private Integer sort_order;
}
