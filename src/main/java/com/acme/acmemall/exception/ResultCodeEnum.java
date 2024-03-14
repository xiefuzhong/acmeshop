package com.acme.acmemall.exception;

public enum ResultCodeEnum {
    SUCCESS(0,"操作成功"),

    FAILED(1,"操作失败"),
    USER_NOT_LOGIN(400, "用户未登录！"),

    UNAUTHORIZED(401, "未登录或token过期，请重新登录！"),

    ORDER_CART_TYPE_ERROR(102,"请先添加到购物车,暂不支持其他方式");

    long code;
    String message;

    public long getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }

    private ResultCodeEnum(long code, String msg){
        this.code = code;
        this.message = msg;
    }
}
