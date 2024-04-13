package com.acme.acmemall.model;

import lombok.Data;
import com.acme.acmemall.utils.DateUtils;

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

    public String getFmtAddTime() {
        if (this.addTime != null && this.addTime > 0) {
            this.fmtAddTime = DateUtils.timeToStr(this.addTime, DateUtils.DATE_TIME_PATTERN);
        }
        return fmtAddTime;
    }

    // 创建时间字符串
    private String fmtAddTime;
    // 更新时间
    private Long updateTime;
    // 操作人ID
    private Long operatorId;

    public void addRole(Long userId) {
        this.addTime = System.currentTimeMillis() / 1000;
        this.fmtAddTime = DateUtils.timeToStr(this.addTime, DateUtils.DATE_TIME_PATTERN);
        this.operatorId = userId;
        this.status = 1;
    }
}
