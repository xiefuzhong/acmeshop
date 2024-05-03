package com.acme.acmemall.model;

import lombok.*;

/**
 * @description:服务类型
 * @author: ihpangzi
 * @time: 2024/1/23 20:21
 */
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
public class Service {
    //    private long expect_time; // 如果是下顺丰散单，则必传此字段，否则不会有收件员上门揽件。
//    private int take_mode; // 分单策略;
    private Integer service_type;
    private String service_name;
}
