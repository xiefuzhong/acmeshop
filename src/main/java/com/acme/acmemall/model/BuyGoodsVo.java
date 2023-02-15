package com.acme.acmemall.model;

import lombok.Data;

import java.io.Serializable;

/**
 * @description:
 * @author: ihpangzi
 * @time: 2023/2/15 14:27
 */
@Data
public class BuyGoodsVo implements Serializable {
    private Integer goodsId;
    private Integer productId;
    private Integer number;
    private String name;
}
