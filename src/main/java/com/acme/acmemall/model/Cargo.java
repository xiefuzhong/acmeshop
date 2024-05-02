package com.acme.acmemall.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

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

    // name count
    private List<Object> detail_list;
}
