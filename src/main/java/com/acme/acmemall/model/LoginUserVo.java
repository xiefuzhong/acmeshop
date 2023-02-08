package com.acme.acmemall.model;

import lombok.*;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;


/**
 * @author admin
 *
 * @date 2017-08-15 08:03:41
 */
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
    private Date registerTime;
    //最后登录时间
    private Date lastLoginTime;
    //最后登录Ip
    private String lastLoginIp;
    //会员等级
    private Integer userLevel;
    //别名
    private String nickname;
    //手机号码
    private String mobile;
    //注册Ip
    private String registerIp;
    //头像
    private String avatar;
    //微信Id
    private String weixinOpenid;

    //身份证号
    private String idCard;
    //推广人id
    private int promoterId;
    //推广人姓名
    private String promoterName;
    //是否实名认证 1：是 2：否
    private String isReal;
    //是否推荐购买返现 0没有、1已返现
    private Integer is_return_cash;
    //首次购买金额
    private BigDecimal firstBuyMoney;
    //推广小程序二维码
    private String qrCode;
    //真实姓名
    private String realName;
}
