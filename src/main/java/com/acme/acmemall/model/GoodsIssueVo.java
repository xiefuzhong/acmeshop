package com.acme.acmemall.model;

import lombok.Data;

import java.io.Serializable;

/**
 * @description:
 * @author: ihpangzi
 * @time: 2023/2/14 8:59
 */
@Data
public class GoodsIssueVo implements Serializable {
    //主键
    private Integer id;
    //商品id
    private String goods_id;
    //问题
    private String question;
    //回答
    private String answer;
}
