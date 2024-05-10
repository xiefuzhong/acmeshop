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
import java.util.List;

/**
 * @description:
 * @author: ihpangzi
 * @time: 2024/5/10 20:44
 */
@Data
public class TraceVo implements Serializable {
    private boolean success;

    private String errorCode;

    private String errorMsg;

    private String mailNo; // 快递单号

    private String cpCode; // 查询的快递公司的编码

    private String logisticsStatus; // 快递单当前主状态

    private String logisticsStatusDesc; // 快递单当前主状态说明

    private String logisticsStatusEx; // 快递单当前详细状态

    private String logisticsStatusExDesc; // 快递单当前详细状态说明

    private String city; // 快递当前所在城市

    private List<FullTraceDetailVo> fullTraceDetail;// 物流轨迹跟踪信息
}
