package com.acme.acmemall.model;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * @description:
 * @author: ihpangzi
 * @time: 2023/2/14 9:02
 */
@Data
public class FootprintVo implements Serializable {
    //主键
    private Integer id;
    //会员Id
    private Long user_id;
    //商品id
    private Integer goods_id;
    //记录时间
    private Long add_time;
    //推荐人
    private Long referrer;

    // 商品冗余字段
    private String name;
    private String list_pic_url;
    private String goods_brief;
    //
    private BigDecimal retail_price;
    // 会员
    private String nickname;
    private String avatar;
}
