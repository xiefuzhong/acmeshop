package com.acme.acmemall.model;

import com.acme.acmemall.utils.Base64;
import com.acme.acmemall.utils.StringUtils;
import com.alibaba.fastjson.JSONArray;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;


@Data
public class LoginUserVo implements Serializable {
    private static final long serialVersionUID = 1L;

    //主键
    private Long userId;
    //会员名称
    private String username;
    //会员密码
    private String password;
    //性别
    private Integer gender;
    //出生日期
    private Date birthday;
    //注册时间
    private Date register_time;
    //最后登录时间
    private Date last_login_time;
    //最后登录Ip
    private String last_login_ip;
    //会员等级
    private Integer user_level_id;
    //别名
    private String nickname;
    //手机号码
    private String mobile;
    //注册Ip
    private String register_ip;
    //头像
    private String avatar;
    //微信Id
    private String weixin_openid;

    //身份证号
    private String idCard;
    //推广人id
    private int promoterId = 0;
    //推广人姓名
    private String promoterName;
    //是否实名认证 1：是 2：否
    private String isReal;
    //是否推荐购买返现 0没有、1已返现
    private Integer is_return_cash;
    //首次购买金额
    private BigDecimal first_buy_money;
    //推广小程序二维码
    private String qrCode;
    //真实姓名
    private String realName;

    private Long merchantId; // 商户ID

    private Long groupId;
    private String groupName;
    private String labels;

    private List<UserLabel> userLabels = Lists.newArrayList();
    public void loginByWeixin(LoginInfo loginInfo, String requestIp) {
        this.username = Base64.encode(loginInfo.getNickName());
        this.password = this.weixin_openid;
        this.register_time = new Date();
        this.register_ip = requestIp;
        this.last_login_ip = requestIp;
        this.last_login_time = new Date();
        this.avatar = loginInfo.getAvatarUrl();
        this.gender = loginInfo.getGender();
        this.promoterId = loginInfo.getPromoterId();
        this.nickname = Base64.encode(loginInfo.getNickName());
    }

    public Map<String, Object> response() {
        Map<String, Object> response = Maps.newHashMap();
        response.put("userId", this.userId);
        response.put("nickname", this.nickname);
        response.put("avatar", this.avatar);
        response.put("mobile", this.mobile);
        return response;

    }

    public void parseLabel() {
        if (StringUtils.isNullOrEmpty(this.labels)) {
            this.userLabels = JSONArray.parseArray(this.labels, UserLabel.class);
        }
    }

    public boolean checkLogin(String pwd) {
        return this.password.equals(pwd);
    }
}
