package com.acme.acmemall.model;

import com.acme.acmemall.utils.DateUtils;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @description:
 * @author: ihpangzi
 * @time: 2024/1/24 14:57
 */

@Data
public class OrderDeliveryTrackVo implements Serializable {
    private long id;
    private String orderId;
    private String waybillId;
    private String latestInfo;
    private String allInfo;
    private Date addTime;
    private String fmt_add_time;
    private Date updateTime;
    private String fmt_update_time;

    public String getFmtAddTime() {
        return DateUtils.timeToUtcDate(this.addTime.getTime(), DateUtils.DATE_TIME_PATTERN);
    }

    public String getFmtUpateTime() {
        return DateUtils.timeToUtcDate(this.updateTime.getTime(), DateUtils.DATE_TIME_PATTERN);
    }
}
