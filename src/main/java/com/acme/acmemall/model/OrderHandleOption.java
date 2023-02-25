package com.acme.acmemall.model;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import lombok.Builder;

import java.util.Map;

/**
 * @description:
 * @author: ihpangzi
 * @time: 2023/2/25 16:10
 */
@Builder
public class OrderHandleOption {
    /**
     * 取消操作
     */
    @Builder.Default
    Boolean cancel = Boolean.FALSE;
    /**
     * 删除操作
     */
    @Builder.Default
    Boolean delete = Boolean.FALSE;
    /**
     * 支付操作
     */
    @Builder.Default
    Boolean pay = Boolean.FALSE;
    /**
     * 评论操作
     */
    @Builder.Default
    Boolean comment = Boolean.FALSE;
    /**
     * 确认收货操作
     */
    @Builder.Default
    Boolean delivery = Boolean.FALSE;
    /**
     * 完成订单操作
     */
    @Builder.Default
    Boolean confirm = Boolean.FALSE;
    /**
     * 退换货操作
     */
    @Builder.Default
    Boolean returned = Boolean.FALSE;

    /**
     * 再次购买
     */
    @Builder.Default
    Boolean buy = Boolean.FALSE;

    /**
     * 根据订单状态，设置订单可操作按钮
     *
     * @param statusCode
     */
    public Map<String, Boolean> canOption(int statusCode) {
        OrderStatus status = OrderStatus.parse(statusCode);
        switch (status) {
            case TO_BE_PAID: {
                this.pay = Boolean.TRUE;
                this.cancel = Boolean.TRUE;
                break;
            }
            case CANCELED: {
                this.delete = Boolean.TRUE;
                this.buy = Boolean.TRUE;
                break;
            }
            case TO_BE_SHIPPED: {
                this.cancel = Boolean.TRUE;
                break;
            }
            case TO_BE_RECEIVED: {
                this.confirm = Boolean.TRUE;
                this.returned = Boolean.TRUE;
                break;
            }
            case COMPLETED: {
                this.comment = Boolean.TRUE;
                this.buy = Boolean.TRUE;
                break;
            }
        }
        return JSON.parseObject(JSON.toJSONString(this), new TypeReference<Map<String, Boolean>>() {
        });
    }
}
