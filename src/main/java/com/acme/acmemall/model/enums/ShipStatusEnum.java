package com.acme.acmemall.model.enums;

/**
 * 物流发货状态
 */
public enum ShipStatusEnum {
    SHIP_NO(0, "未发货"),
    SHIP_YES(1, "已发货"),
    SHIP_ROG(2, "已收货"),
    SHIP_CANCE_SUCCESS(3, "拦截成功"), // 拦截成功
    SHIP_CANCEL_FAILED(5, "拦截失败"), // 拦截失败
    SHIP_RETURN(4, "退还商品");

    //    发货状态 商品配送情况;0未发货,1已发货,2已收货,4退货,3拦截成功 5拦截失败不可退款。
    private final String name;
    private final int code;

    public String getName() {
        return name;
    }

    public int getCode() {
        return code;
    }

    ShipStatusEnum(int code, String name) {
        this.code = code;
        this.name = name;
    }

    public static ShipStatusEnum parse(int code) {
        if (code <= 0) {
            return SHIP_NO;
        }
        for (ShipStatusEnum status : values()) {
            if (code == status.getCode()) {
                return status;
            }
        }
        return SHIP_NO;
    }

}
