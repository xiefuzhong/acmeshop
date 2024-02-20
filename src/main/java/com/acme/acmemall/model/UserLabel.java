package com.acme.acmemall.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @description:
 * @author: ihpangzi
 * @time: 2024/2/20 10:04
 */
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserLabel implements Serializable {
    private long id;
    private String title;
    private Integer type;
}
