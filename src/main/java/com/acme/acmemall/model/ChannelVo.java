package com.acme.acmemall.model;

import lombok.Data;

import java.io.Serializable;

@Data
public class ChannelVo implements Serializable {
    private Integer id;

    private String name;

    private String url;

    private String iconUrl;

    private Integer sortOrder;
}
