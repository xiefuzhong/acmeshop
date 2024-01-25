package com.acme.acmemall.model;

import lombok.Data;

import java.io.Serializable;

/**
 * @description:
 * @author: ihpangzi
 * @time: 2024/1/25 11:53
 */
@Data
public class SpecificationVo implements Serializable {
    private long id;
    private String name;
    private Integer enable;
    private Integer sortOrder;
    private long merchantId;
}
