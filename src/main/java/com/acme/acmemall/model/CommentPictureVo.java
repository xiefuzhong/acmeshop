package com.acme.acmemall.model;

import lombok.Data;

import java.io.Serializable;

/**
 * @description:
 * @author: ihpangzi
 * @time: 2023/2/14 9:01
 */
@Data
public class CommentPictureVo implements Serializable {
    //主键
    private Integer id;
    //评价Id
    private Integer comment_id;
    //评价图片
    private String pic_url;
    //排序
    private Integer sort_order;
}
