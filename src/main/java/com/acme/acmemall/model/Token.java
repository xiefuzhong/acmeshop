package com.acme.acmemall.model;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 用户访问token
 */
@Data
public class Token implements Serializable {
    private Long userId;
    //token
    private String token;
    //过期时间
    private Date expireTime;
    //更新时间
    private Date updateTime;
}
