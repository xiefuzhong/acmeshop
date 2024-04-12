package com.acme.acmemall.model;

import lombok.Data;

/**
 * @description:角色VO
 * @author: ihpangzi
 * @time: 2024/4/12 17:12
 */
@Data
public class RoleVo {
    // 角色ID
    private Long id;
    // 角色名称
    private String roleName;
    // 角色描述
    private String description;
    // 状态
    private Integer status;
    //创建时间
    private Long addTime;
    // 更新时间
    private Long updateTime;
    // 操作人ID
    private Long operatorId;
}
