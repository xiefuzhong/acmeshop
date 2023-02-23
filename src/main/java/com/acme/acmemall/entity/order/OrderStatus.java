package com.acme.acmemall.entity.order;

public enum OrderStatus {
    NON_PAYMENT(0),
    SHIPPED(2),
    FINISHED(5),
    CANCELED(6);

    Integer code;
    OrderStatus(Integer code){
        this.code = code;
    }

    public Integer getCode() {
        return code;
    }
}
