/*
 * Copyright (c) 2024. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */

package com.acme.acmemall.cainiao.vo;

import lombok.Data;

import java.io.Serializable;

/**
 * @description:
 * @author: ihpangzi
 * @time: 2024/5/10 20:43
 */
@Data
public class FullTraceDetailVo implements Serializable {
    private String desc; // 物流说明内容

    private String time; // 物流发生时间格式yyyy-MM-dd HH:mm:ss

    private String city; // 城市

    private String status; // 物流状态

    private String status_text;

    private String deliveryMan; // 派件快递员（只有派件节点才会有值）

    private String deliveryManTel; // 派件快递员手机号（只有派件节点才会有值）

    public String getStatus_text() {
        return status_text;
    }
}
