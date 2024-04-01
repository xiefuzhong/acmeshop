package com.acme.acmemall.model;

import lombok.Data;

import java.io.Serializable;

/**
 * @description:
 * @author: ihpangzi
 * @time: 2024/4/1 14:03
 */
@Data
public class UserGroup implements Serializable {
    private long id;
    private String group_name;
}
