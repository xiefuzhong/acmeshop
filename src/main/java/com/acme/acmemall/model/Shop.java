package com.acme.acmemall.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * @description:商品信息，会展示到物流服务通知和电子面单中
 * @author: ihpangzi
 * @time: 2024/1/23 20:20
 */
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
public class Shop {
    private String wxa_path; // 商家小程序的路径，建议为订单页面

    private String img_url; // 商品缩略图 url；shop.detail_list为空则必传，shop.detail_list非空可不传。

    private String goods_name; // 商品名称, 不超过128字节；shop.detail_list为空则必传，shop.detail_list非空可不传。

    private int goods_count; // 商品数量；shop.detail_list为空则必传。shop.detail_list非空可不传，默认取shop.detail_list的size
}
