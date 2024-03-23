package com.acme.acmemall.model.enums;

public enum CouponTypeEnum {

    DISCOUNT_PRICE(0, "满减券"),
    DISCOUNT_RATE(1, "折扣券");

    private final int type;
    private final String title;

    public int getType() {
        return type;
    }

    public String getTitle() {
        return title;
    }

    CouponTypeEnum(int type, String title) {
        this.type = type;
        this.title = title;
    }

    /**
     * 根据类型获取枚举
     *
     * @param type
     * @return
     */
    public static CouponTypeEnum parse(int type) {
        for (CouponTypeEnum couponTypeEnum : CouponTypeEnum.values()) {
            if (couponTypeEnum.getType() == type) {
                return couponTypeEnum;
            }
        }
        return null;
    }

    /**
     * 校验是否是有效值
     *
     * @param type
     * @return
     */
    public static Boolean verify(int type) {
        return parse(type) != null;
    }
}
