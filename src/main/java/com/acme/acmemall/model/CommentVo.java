package com.acme.acmemall.model;

import com.acme.acmemall.utils.DateUtils;
import com.alibaba.fastjson.JSONObject;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

/**
 * @description:
 * @author: ihpangzi
 * @time: 2023/2/14 9:00
 */
@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor
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

    private String fmt_add_time;
    //状态 是否被管理员批准显示;1是;0未批准显示
    private Integer status;
    //会员Id
    private Long user_id;

    //会员Id
    private LoginUserVo user_info;
    private List<CommentPictureVo> pic_list;

    /**
     * 发表评论
     *
     * @param object
     */
    public void post(JSONObject object) {
        this.add_time = System.currentTimeMillis() / 1000;
        this.status = 0;
        this.type_id = object.getInteger("type_id");
        this.value_id = object.getInteger("value_id");
        this.content = object.getString("content");
    }

    public void resetShow(LoginUserVo userVo) {
        this.user_info = userVo;
//        this.content = Base64.encode(this.content);
        this.fmt_add_time = DateUtils.timeToStr(this.add_time, DateUtils.DATE_TIME_PATTERN);
    }
}
