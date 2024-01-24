package com.acme.acmemall.kuaidi100.response;

import lombok.Data;

/**
 * @description:
 * @author: ihpangzi
 * @time: 2024/1/24 11:00
 */
@Data
public class QueryTrackPosition {

    /**
     * 地址编码
     */
    private String number;
    /**
     * 地址名称
     */
    private String name;
}
