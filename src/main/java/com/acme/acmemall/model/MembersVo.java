package com.acme.acmemall.model;

import lombok.Data;

import java.io.Serializable;

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
    private long roleId;
    private String roleName;
    private String roleDesc;
    private Integer roleType;
}
