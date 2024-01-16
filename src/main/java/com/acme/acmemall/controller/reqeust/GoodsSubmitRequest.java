package com.acme.acmemall.controller.reqeust;

import com.acme.acmemall.exception.ApiCusException;
import com.acme.acmemall.model.GoodsGalleryVo;
import com.acme.acmemall.model.GoodsSpecificationVo;
import com.acme.acmemall.model.GoodsVo;
import com.acme.acmemall.model.ProductVo;
import com.google.common.collect.Lists;
import lombok.Data;
import org.apache.commons.collections.CollectionUtils;

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

    public void validate() {
        if (!getGoods().check()) {
            throw new ApiCusException("名称不能为空");
        }
        if (CollectionUtils.isEmpty(galleryList)) {
            throw new ApiCusException("轮播信息不能为空");
        }
        if (CollectionUtils.isEmpty(specList)) {
            throw new ApiCusException("规格信息不能为空");
        }
        if (CollectionUtils.isEmpty(products)) {
            throw new ApiCusException("产品库存信息不能为空");
        }
    }
}
