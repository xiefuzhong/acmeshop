package com.acme.acmemall.model;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @description:
 * @author: ihpangzi
 * @time: 2023/2/14 9:00
 */
@Data
public class CommentVo implements Serializable {
    //主键
    private Integer id;
    //用户评论的类型;0评论的是商品,1评论的是文章
    private Integer type_id;
    //产品Id
    private Integer value_id;
    //储存为base64编码
    private String content;
    //记录时间
    private Long add_time;
    //状态 是否被管理员批准显示;1是;0未批准显示
    private Integer status;
    //会员Id
    private Long user_id;

    //会员Id
    private LoginUserVo user_info;
    private List<CommentPictureVo> pic_list;
}
