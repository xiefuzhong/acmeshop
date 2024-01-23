package com.acme.acmemall.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * @description:发件人信息
 * @author: ihpangzi
 * @time: 2024/1/23 20:20
 */
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
public class Receiver {
    private String name;

    private String tel;

    private String mobile;

    private String company;

    private String post_code;

    private String country;

    private String province;

    private String city;

    private String area;

    private String address;
}
