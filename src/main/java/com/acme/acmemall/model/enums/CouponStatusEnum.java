/*
 * Copyright (c) 2024. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */

package com.acme.acmemall.model.enums;

public enum CouponStatusEnum {
    AVAILABLE(1, "可使用"),
    NO_AVAILABLE(4, "已用完"),
    EXPIRED(3, "已过期");

    private final int code;
    private final String title;

    public int getCode() {
        return code;
    }

    public String getTitle() {
        return title;
    }

    CouponStatusEnum(int code, String title) {
        this.code = code;
        this.title = title;
    }
}
