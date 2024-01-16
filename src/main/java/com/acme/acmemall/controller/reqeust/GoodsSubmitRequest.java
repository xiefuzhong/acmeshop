package com.acme.acmemall.controller.reqeust;

import com.acme.acmemall.model.GoodsGalleryVo;
import com.acme.acmemall.model.GoodsSpecificationVo;
import com.acme.acmemall.model.GoodsVo;
import com.acme.acmemall.model.ProductVo;
import com.google.common.collect.Lists;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @description:
 * @author: ihpangzi
 * @time: 2024/1/15 14:11
 */
@Data
public class GoodsSubmitRequest implements Serializable {

    private GoodsVo goods; // 商品信息
    private List<GoodsGalleryVo> galleryList = Lists.newArrayList(); // 产品轮播信息
    private List<GoodsSpecificationVo> specList = Lists.newArrayList(); // 规格信息
    private List<ProductVo> products = Lists.newArrayList(); // SKU信息

}
