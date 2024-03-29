package com.acme.acmemall.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @description:
 * @author: ihpangzi
 * @time: 2024/3/29 11:16
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class GoodsCouponVo implements Serializable {
    private long goodsId;
    private long couponId;
}
