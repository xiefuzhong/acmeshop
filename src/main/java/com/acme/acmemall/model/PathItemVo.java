/*
 * Copyright (c) 2024. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */

package com.acme.acmemall.model;

import com.acme.acmemall.model.enums.TrackNodeType;
import com.acme.acmemall.utils.DateUtils;
import lombok.Data;

import java.io.Serializable;

/**
 * @description:
 * @author: ihpangzi
 * @time: 2024/5/8 13:33
 */

@Data
public class PathItemVo implements Serializable {

    private Long action_time;
    private String action_time_text;
    private Integer action_type;
    private String action_type_text;
    private String action_msg;

    public String getAction_time_text() {
        if (action_time != null) {
            action_time_text = DateUtils.timeToStr(this.action_time, DateUtils.DATE_TIME_PATTERN);
        }
        return action_time_text;
    }

    public String getAction_type_text() {
        return TrackNodeType.parse(action_type).getTitle();
    }

}
