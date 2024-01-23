package com.acme.acmemall.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * @description:包裹信息，将传递给快递公司
 * @author: ihpangzi
 * @time: 2024/1/23 20:20
 */
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
public class Cargo {

    private String count;

    private String weight;

    private String space_x;

    private String space_y;

    private String space_z;
}
