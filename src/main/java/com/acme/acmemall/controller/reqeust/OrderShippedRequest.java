package com.acme.acmemall.controller.reqeust;

import com.acme.acmemall.model.AddressVo;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * @description:
 * @author: ihpangzi
 * @time: 2024/1/22 21:50
 */
@Getter
@Setter
public class OrderShippedRequest implements Serializable {
    private String orderId;
    private String handle;
    private LogisticsInfo LogisticsInfo;
    private AddressVo shippingAddress;
    private AddressVo returnAddress;
}
