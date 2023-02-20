package com.acme.acmemall.model;

import lombok.Data;

import java.io.Serializable;

/**
 * @description:
 * @author: ihpangzi
 * @time: 2023/2/18 16:41
 */
@Data
public class RegionVo implements Serializable {
    //主键
    private Integer id;
    //父节点
    private Integer parentId;
    //区域名称
    private String name;
    // 类型 0国家 1省份 2地市 3区县
    private Integer type;
    //区域代理Id
    private Integer agency_id;
}
