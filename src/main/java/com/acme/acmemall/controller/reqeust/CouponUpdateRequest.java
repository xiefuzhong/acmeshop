package com.acme.acmemall.controller.reqeust;

import java.io.Serializable;

/**
 * @description:
 * @author: ihpangzi
 * @time: 2024/3/25 17:07
 */
public class CouponUpdateRequest implements Serializable {
    private Long couponId;

    private Integer send_type;

    private Long userId;
}
