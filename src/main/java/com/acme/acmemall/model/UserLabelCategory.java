package com.acme.acmemall.model;

import lombok.Data;

import java.io.Serializable;

/**
 * @description:
 * @author: ihpangzi
 * @time: 2024/4/1 15:04
 */
@Data
public class UserLabelCategory implements Serializable {
    private Long id;
    private String title;
    private Integer sort;
    //    分类类型0=标签分类，1=快捷短语分类
    private Integer type;
    private long add_time;
}
