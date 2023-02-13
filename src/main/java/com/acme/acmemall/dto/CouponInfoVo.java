package com.acme.acmemall.dto;

import lombok.Data;

/**
 * @description:优惠券信息
 * @author: ihpangzi
 * @time: 2023/2/13 11:52
 */
@Data
public class CouponInfoVo {
    private String msg; // 显示信息
    private Integer type = 0; // 是否凑单 0否 1是

    public String fmtMsg(){
        return null;
    }
}
