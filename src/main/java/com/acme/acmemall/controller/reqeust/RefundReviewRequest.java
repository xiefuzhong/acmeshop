package com.acme.acmemall.controller.reqeust;

import lombok.Data;

import java.io.Serializable;

/**
 * @description:
 * @author: ihpangzi
 * @time: 2024/3/9 15:16
 */
@Data
public class RefundReviewRequest implements Serializable {
    private String orderId;
    private String handle;
    private String refuse_reason;
}
