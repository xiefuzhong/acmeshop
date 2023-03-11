package com.acme.acmemall.model;

import com.acme.acmemall.utils.GsonUtil;
import com.alibaba.fastjson.JSONObject;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * @description:
 * @author: ihpangzi
 * @time: 2023/3/10 17:25
 */
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserGoods implements Serializable {
    private Integer userGoodsId;
    private Long userId;
    private Integer goodsId;
    private String name;
    private String goodsBrief;
    private BigDecimal retailPrice;
    private BigDecimal marketPrice;
    private String primaryPicUrl;

    public void bindQueryParam(Long userId, Integer goodsId) {
        this.userId = userId;
        this.goodsId = goodsId;
    }

    public void add(JSONObject object) {
        this.goodsId = object.getInteger("goodsId");
        this.name = object.getString("name");
        this.goodsBrief = object.getString("goodsBrief");
        this.retailPrice = object.getBigDecimal("retailPrice");
        this.marketPrice = object.getBigDecimal("marketPrice");
        this.primaryPicUrl = object.getString("primaryPicUrl");
    }

    @Override
    public String toString() {
        return GsonUtil.toJson(this);
    }
}
