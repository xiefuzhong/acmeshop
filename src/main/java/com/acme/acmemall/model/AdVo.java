package com.acme.acmemall.model;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.Date;


/**
 * 广告信息
 * @author IHPANGZI
 * @date 2023-02-08
 */
@Getter
@Setter
public class AdVo implements Serializable {
    private static final long serialVersionUID = 1L;

    //主键
    private Integer id;
    //广告位置Id
    private Integer adPositionId;
    //形式
    private Integer mediaType;
    //广告名称
    private String name;
    //链接
    private String link;
    //图片
    private String imageUrl;
    //内容
    private String content;
    //结束时间
    private Date endTime;
    //状态
    private Integer enabled;
}
