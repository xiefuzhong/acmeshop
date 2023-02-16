package com.acme.acmemall.model;

import lombok.Data;

import java.io.Serializable;

/**
 * @description: 历史搜索记录
 * @author: ihpangzi
 * @time: 2023/2/16 9:30
 */
@Data
public class SearchHistoryVo implements Serializable {

    //主键
    private Integer id;
    //关键字
    private String keyword;

    //搜索来源，如PC、小程序、APP等
    private String from;
    //搜索时间
    private Long add_time;
    //会员Id
    private String user_id;
}
