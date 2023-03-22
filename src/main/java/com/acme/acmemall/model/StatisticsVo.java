package com.acme.acmemall.model;

import lombok.Data;

import java.io.Serializable;

/**
 * @description:指标统计
 * @author: ihpangzi
 * @time: 2023/3/22 14:22
 */
@Data
public class StatisticsVo implements Serializable {
    private String type;
    private Integer num;
}
