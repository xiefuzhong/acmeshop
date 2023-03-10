package com.acme.acmemall.model;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

@Data
public class TopicVo implements Serializable {
    private static final long serialVersionUID = 1L;

    //主键
    private Integer id;
    //活动主题
    private String title;
    //活动内容
    private String content;
    //化名
    private String avatar;
    //活动条例图片
    private String item_pic_url;
    //子标题
    private String subtitle;
    //活动类别
    private Integer topic_category_id;
    //活动价格
    private BigDecimal price_info;
    //
    private String read_count;
    //场景图片链接
    private String scene_pic_url;
    //活动模板Id
    private Integer topic_template_id;
    //活动标签Id
    private Integer topic_tag_id;
}
