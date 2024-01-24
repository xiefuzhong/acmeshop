package com.acme.acmemall.kuaidi100.response;

import lombok.Data;

@Data
public class BaseResponse<T> {
    private boolean result;

    private String message;

    private String status;

    private T data;
}
