package com.acme.acmemall.model;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * @description:
 * @author: ihpangzi
 * @time: 2023/3/10 17:25
 */
@Data
public class UserGoods implements Serializable {
    private Integer userGoodsId;
    private Long userId;
    private Integer goodsId;
    private String name;
    private String goodsBrief;
    private BigDecimal retailPrice;
    private BigDecimal marketPrice;
    private String primaryPicUrl;

}
