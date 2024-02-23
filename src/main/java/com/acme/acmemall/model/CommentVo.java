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
    //用户评论的类型; 0-针对商品 1-针对评论
    private Integer type_id;
    //0-商品Id,1-评论ID
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

    private String order_id;
    private Integer reply_flag;
    private Integer del_flag;

    private String mer_reply_content;
    private Long mer_reply_time;
    private String goods_name;
    private String goods_spec;
    private String goods_pic;
    private String nick_name;
    private String avatar;

    /**
     * 发表评论
     *
     * @param object
     */
    public void post(JSONObject object) {
        this.add_time = System.currentTimeMillis() / 1000;
        this.fmt_add_time = DateUtils.timeToStr(this.add_time, DateUtils.DATE_TIME_PATTERN);
        this.status = 0;
        this.type_id = object.getInteger("type_id");
        this.value_id = object.getInteger("value_id");
        this.goods_name = object.getString("goods_name");
        this.goods_spec = object.getString("goods_spec");
        this.goods_pic = object.getString("goods_pic");

        this.content = object.getString("content");
        this.order_id = object.getString("order_id");
        this.reply_flag = 0;
        this.del_flag = 0;

    }

    public void reply(JSONObject object) {
        this.reply_flag = 1;
        this.mer_reply_content = object.getString("content");
        this.mer_reply_time = System.currentTimeMillis() / 1000;
    }

    public void resetShow(LoginUserVo userVo) {
        this.user_info = userVo;
//        this.content = Base64.encode(this.content);
        this.fmt_add_time = DateUtils.timeToStr(this.add_time, DateUtils.DATE_TIME_PATTERN);
        this.nick_name = userVo.getNickname();
        this.avatar = userVo.getAvatar();
    }
}
