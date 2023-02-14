package com.acme.acmemall.model;

import lombok.Data;

import java.io.Serializable;

/**
 * @description:
 * @author: ihpangzi
 * @time: 2023/2/14 10:17
 */
@Data
public class CollectVo implements Serializable {
    //主键
    private Integer id;
    //用户Id
    private Long user_id;
    //产品Id
    private Integer value_id;
    //添加时间
    private Long add_time;
    //是否是关注
    private Integer is_attention;
    //
    private Integer type_id;
    //
    private String name;
    private String list_pic_url;
    private String goods_brief;
    private String retail_price;
}
