package com.acme.acmemall.common;

import java.io.Serializable;

public abstract class TimeConstants implements Serializable {


    /**
     * 时间单位：1秒
     */
    private final static Long TIME_S = 1000L;

    /**
     * 时间单位：1分钟=60秒
     */
    private final static Long TIME_MINUTE = TIME_S * 60;

    /**
     * 时间单位：1小时=60分钟
     */
    private final static Long TIME_HOUR = TIME_MINUTE * 60;

    /**
     * 时间单位：1天=24小时
     */
    private final static Long TIME_DAY = TIME_HOUR * 24;

    /**
     * 支付过期时间：自提交订单起30分钟后自动取消
     */
    public final static Long PAY_EXPIRE_TIME = TIME_MINUTE * 30;

    /**
     * 售后时效时间：自收货之日起30天后不许发起售后
     */
    public final static Long AFTER_SALES_EXPIRE_TIME = TIME_DAY * 30;

    /**
     * 评价超时：15天后自动好评
     */
    public final static Long EVALUATE_EXPIRE_TIME = TIME_DAY * 15;

    /**
     * 自动确认收货时间：自发货之日起10天后自动确认收货
     */
    public final static Long AUTO_RECEIPT_TIME = TIME_DAY * 10;

}
