package com.acme.acmemall.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;

import java.util.Date;

/**
 * @description:
 * @author: ihpangzi
 * @time: 2024/3/12 19:15
 */
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
public class OrderProcessVo {
    // 订单处理时间
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date process_time;

    // 操作描述(生成订单、支付、发货、收货、退款、退货、关闭)
    private String process_desc;

    private Integer sort_id;
}
