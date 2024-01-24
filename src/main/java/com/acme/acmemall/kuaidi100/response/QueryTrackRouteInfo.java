package com.acme.acmemall.kuaidi100.response;

import lombok.Data;

/**
 * @description:
 * @author: ihpangzi
 * @time: 2024/1/24 10:59
 */
@Data
public class QueryTrackRouteInfo {
    /**
     * 出发位置
     */
    private QueryTrackPosition from;
    /**
     * 当前位置
     */
    private QueryTrackPosition cur;
    /**
     * 收货地
     */
    private QueryTrackPosition to;
}
