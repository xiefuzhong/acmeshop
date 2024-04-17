package com.acme.acmemall.model;

import com.acme.acmemall.utils.DateUtils;
import com.alibaba.fastjson.JSONObject;
import lombok.Data;

import java.io.Serializable;
import java.util.List;
import java.util.Objects;

/**
 * @description:
 * @author: ihpangzi
 * @time: 2024/4/15 10:44
 */
@Data
public class MembersVo implements Serializable {
    private long userId;
    private String userName;
    private String avatar;
    private String email;
    private String mobile;
    private String password;
    private Long addTime;

    // 创建时间字符串
    private String fmtAddTime;

    private Long updateTime;
    private Long operatorId;
    private Integer status;
    List<RoleVo> roles;

    public String getFmtAddTime() {
        if (this.addTime != null && this.addTime > 0) {
            this.fmtAddTime = DateUtils.timeToStr(this.addTime, DateUtils.DATE_TIME_PATTERN);
        }
        return fmtAddTime;
    }

    public void addMember(Long userId, String pwd, Integer status) {
        this.addTime = System.currentTimeMillis() / 1000;
        this.password = pwd;
        this.operatorId = userId;
        this.status = status == null ? 0 : status;
    }

    public void grantPermissions() {
        this.updateTime = System.currentTimeMillis() / 1000;
        this.operatorId = 0L;
//        this.roleId = 0L;
//        this.roleName = "";
    }

    public void updateMember(Long userId, JSONObject json) {
        this.updateTime = System.currentTimeMillis() / 1000;
        if (json.containsKey("password") && json.containsKey("confirmPassword") && Objects.equals(json.getString("password"), json.getString("confirmPassword"))) {
            this.password = json.getString("password");
        }
        if (json.containsKey("status")) {
            this.status = json.getInteger("status");
        }
        this.operatorId = userId;
    }
}
