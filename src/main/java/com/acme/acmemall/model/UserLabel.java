package com.acme.acmemall.model;

import lombok.Data;

import java.io.Serializable;

/**
 * @description:
 * @author: ihpangzi
 * @time: 2024/2/20 10:04
 */
@Data
public class UserLabel implements Serializable {
    //    [{"checked":true,"id":2,"title":"设计总监"}]
    private long id;
    private String title;
    private boolean checked;
}
