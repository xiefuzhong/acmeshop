package com.acme.acmemall.model;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @description:运单轨迹
 * @author: ihpangzi
 * @time: 2024/5/8 13:31
 */

@Data
public class WaybillTrackVo implements Serializable {
    private String openid;
    private String delivery_id;
    private String waybill_id;
    private int path_item_num;
    private List<PathItemVo> path_item_list;
}
