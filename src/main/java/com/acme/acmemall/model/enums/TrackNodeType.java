package com.acme.acmemall.model.enums;

/**
 * 轨迹节点类型
 *
 * @author: ihpangzi
 */
public enum TrackNodeType {
    PICKUP_SUCCESS(100001, "揽件阶段-揽件成功"),
    PICKUP_FAIL(100002, "揽件阶段-揽件失败"),
    PICKUP_ASSIGN(100003, "揽件阶段-分配业务员"),
    TRACK_UPDATE(200001, "运输阶段-更新运输轨迹"),
    DELIVERY_START(300002, "派送阶段-开始派送"),
    DELIVERY_SIGN_SUCCESS(300003, "派送阶段-签收成功"),
    DELIVERY_SIGN_FAIL(300004, "派送阶段-签收失败"),
    EXCEPTION_CANCEL(400001, "异常阶段-订单取消"),
    EXCEPTION_DELAY(400002, "异常阶段-订单滞留");

    private final Integer code;

    private final String title;

    public Integer getCode() {
        return code;
    }

    public String getTitle() {
        return title;
    }

    TrackNodeType(Integer code, String title) {
        this.code = code;
        this.title = title;
    }

    public static TrackNodeType parse(Integer code) {
        for (TrackNodeType type : TrackNodeType.values()) {
            if (type.getCode().equals(code)) {
                return type;
            }
        }
        return null;
    }
}
