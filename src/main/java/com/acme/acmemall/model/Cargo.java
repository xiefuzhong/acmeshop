package com.acme.acmemall.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

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

    private Integer count;

    private BigDecimal weight;

    private BigDecimal space_x;

    private BigDecimal space_y;

    private BigDecimal space_z;
}
