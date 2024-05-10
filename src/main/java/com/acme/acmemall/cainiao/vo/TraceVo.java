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

    private String mailNo;

    private String cpCode;

    private String logisticsStatus;

    private String logisticsStatusDesc;

    private String logisticsStatusEx;

    private String logisticsStatusExDesc;

    private String city;

    private List<FullTraceDetailVo> fullTraceDetail;
}
