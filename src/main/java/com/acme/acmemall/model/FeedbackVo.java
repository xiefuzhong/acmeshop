package com.acme.acmemall.model;

import com.alibaba.fastjson.JSONObject;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.io.Serializable;
import java.util.Date;

/**
 * @description:
 * @author: ihpangzi
 * @time: 2023/3/14 9:16
 */
@AllArgsConstructor
@Getter
@Builder
public class FeedbackVo implements Serializable {
    //主键
    private Integer msgId;
    //父节点
//    private Integer parentId;
    //会员Id
    private Long userId;
    //会员名称
    private String userName;

    //移动电话
    private String mobile;
    //邮件
//    private String userEmail;
    //标题
//    private String msgTitle;
    //类型
    private Integer feedType;
    //状态
    private Integer status;
    //详细内容
    private String content;
    //反馈时间
    private Date addTime;
    //图片
//    private String messageImg;
    //订单Id
//    private Integer orderId;
    //
//    private Integer msgArea;

    public void feedBack(LoginUserVo userVo, JSONObject obj) {
        this.addTime = new Date();
        this.status = 1;
        this.userId = userVo.getUserId();
        this.userName = userVo.getUsername();
        this.feedType = obj.getInteger("index");
        this.content = obj.getString("content");
        this.mobile = obj.getString("mobile");
    }
}
