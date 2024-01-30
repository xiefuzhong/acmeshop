package com.acme.acmemall.common;

import com.acme.acmemall.utils.StringUtils;

public enum GoodsHandleType {
    /**
     * off：下架
     */
    OFF("off"),
    /**
     * draft:草稿
     */
    DRAFT("draft"),
    /**
     * 删除
     */
    DELETE("delete"),
    /**
     * on:上架
     */
    ON("on");


    private final String type;

    public String getType() {
        return type;
    }

    GoodsHandleType(String type) {
        this.type = type;
    }

    /**
     * 校验type 是不是在范围内
     *
     * @param type
     * @return
     */
    public static boolean validate(String type) {
        if (type == null || StringUtils.isNullOrEmpty(type)) {
            return Boolean.FALSE;
        }
        for (GoodsHandleType handleType : values()) {
            if (type.equalsIgnoreCase(handleType.getType())) {
                return Boolean.TRUE;
            }
        }
        return Boolean.FALSE;
    }

    public static GoodsHandleType parse(String type) {
        if (type == null || StringUtils.isNullOrEmpty(type)) {
            return OFF;
        }
        for (GoodsHandleType handleType : values()) {
            if (type.equalsIgnoreCase(handleType.getType())) {
                return handleType;
            }
        }
        return OFF;
    }
}
