package com.acme.acmemall.model;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @description: 分类
 * @author: ihpangzi
 * @time: 2023/2/14 8:57
 */
@Data
public class CategoryVo implements Serializable {
    //主键
    private Integer id;
    //分类名称
    private String name;
    //关键字
    private String keywords;
    //描述
    private String frontDesc;
    //父节点
    private Integer parentId;
    //排序
    private Integer sortOrder;
    //首页展示
    private Integer showIndex;
    //显示
    private Integer showFlag;
    //banner图片
    private String bannerUrl;
    //icon链接
    private String iconUrl;
    //图片
    private String imgUrl;
    //手机banner
    private String wapBannerUrl;
    //级别
    private String level;
    //类型 0为产品，1-配件
    private Integer type;
    //
    private String frontName;

    private Boolean checked;

    private List<CategoryVo> subCategoryList;

    private List<GoodsVo> goods;
}
