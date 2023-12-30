package com.acme.acmemall.model;

import lombok.Data;

import java.io.Serializable;

/**
 * @description:
 * @author: ihpangzi
 * @time: 2023/12/30 16:13
 */

@Data
public class RelatedGoodsVo implements Serializable {

    //主键
    private Integer id;
    //商品Id
    private Integer goods_id;
    //关联商品id(配件)
    private Integer related_goods_id;

    // 1-成品 2-配件
    private Integer type;
}
