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
    private long id;
    private String title;
    // 标签分类
    private long category_name;
    private long category_id;
}
