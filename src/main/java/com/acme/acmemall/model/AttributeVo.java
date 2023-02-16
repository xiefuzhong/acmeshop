package com.acme.acmemall.model;

import lombok.Data;

import java.io.Serializable;

/**
 * @description:属性
 * @author: ihpangzi
 * @time: 2023/2/14 8:57
 */
@Data
public class AttributeVo implements Serializable {
    private Integer id;
    //
    private Integer attribute_category_id;
    //
    private String name;
    //
    private Integer input_type;
    //
    private String value;
    //
    private Integer sort_order;
}
