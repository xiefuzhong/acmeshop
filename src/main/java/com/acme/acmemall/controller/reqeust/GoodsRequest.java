package com.acme.acmemall.controller.reqeust;

import com.google.common.collect.Lists;
import lombok.Data;

import java.io.Serializable;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

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
    private String primary_pic_url; //  主图链接
    private String list_pic_url; // 列表缩略图链接
    private List<MaterialVo> detailImgList = Lists.newArrayList(); // 详情图
    private long merchantId;    // 商户ID
    private static final String END = "<p><br></p>";

    public String getGoodsDesc() {
        StringBuffer buffer = new StringBuffer();
        // 按年龄从小到大排序
        Comparator<MaterialVo> sortIdComparator = Comparator.comparingInt(MaterialVo::getSortId);
        this.detailImgList = this.detailImgList.stream().sorted(sortIdComparator).collect(Collectors.toList());
        this.detailImgList.stream().forEach(materialVo -> buffer.append(materialVo.getFmtHtml()));
        return buffer.append("<p><br></p>").toString();
    }
}
