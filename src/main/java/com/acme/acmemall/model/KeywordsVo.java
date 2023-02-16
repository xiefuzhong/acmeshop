package com.acme.acmemall.model;

import lombok.Data;

import java.io.Serializable;

/**
 * @description:关键字词表
 * @author: ihpangzi
 * @time: 2023/2/16 9:29
 */
@Data
public class KeywordsVo implements Serializable {
    //关键字
    private String keyword;
    //热销
    private Integer is_hot;
    //默认
    private Integer is_default;
    //显示
    private Integer is_show;
    //排序
    private Integer sort_order;
    //关键词的跳转链接
    private String scheme_url;
    //主键
    private Integer id;
    //类型
    private Integer type;
}
