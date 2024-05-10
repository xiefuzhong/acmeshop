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
    private String desc;

    private String time;

    private String city;

    private String status;

    private String status_text;

    public String getStatus_text() {
        return status_text;
    }
}
