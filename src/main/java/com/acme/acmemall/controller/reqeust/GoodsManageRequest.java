package com.acme.acmemall.controller.reqeust;

import com.acme.acmemall.model.ProductVo;
import com.google.common.collect.Lists;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @description:
 * @author: ihpangzi
 * @time: 2024/1/17 11:52
 */
@Data
public class GoodsManageRequest implements Serializable {
    //    商品ID,支持单个或批量，以","分隔,场景：设置运费/下架
    private String goodsIds;
    //    操作（商品上下架，草稿更新(修改库存、修改价格)）
    private String handle;

    // 改标题，描述、关键字
    private GoodsRequest goods;
    // 改库存、数量
    private List<ProductVo> productList = Lists.newArrayList();
}
