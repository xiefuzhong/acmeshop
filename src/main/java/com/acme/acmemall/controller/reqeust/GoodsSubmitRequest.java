package com.acme.acmemall.controller.reqeust;

import com.acme.acmemall.model.GoodsGalleryVo;
import com.acme.acmemall.model.GoodsSpecificationVo;
import com.acme.acmemall.model.ProductVo;
import com.acme.acmemall.pojo.Sku;
import com.acme.acmemall.utils.GsonUtil;
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

    private GoodsRequest goods; // 商品信息
    private List<GoodsGalleryVo> galleryList = Lists.newArrayList(); // 产品轮播信息
    private List<GoodsSpecificationVo> specList = Lists.newArrayList(); // 规格信息
    private List<ProductVo> products = Lists.newArrayList(); // SKU信息
    private List<Sku> skuList = Lists.newArrayList();

    @Override
    public String toString() {
        return GsonUtil.getGson().toJson(this);
    }
}
