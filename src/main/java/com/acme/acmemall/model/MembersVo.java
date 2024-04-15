package com.acme.acmemall.model;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

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
    //    private long roleId;
//    private String roleName;
//    private String roleDesc;
//    private Integer roleType;
    private Long addTime;
    private Long updateTime;
    private Long operatorId;
    private Integer status;
    List<RoleVo> roles;

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

}
