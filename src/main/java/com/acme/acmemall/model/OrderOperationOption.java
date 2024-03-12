package com.acme.acmemall.model;

import com.acme.acmemall.model.enums.OrderStatusEnum;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Map;

/**
 * @description:
 * @author: ihpangzi
 * @time: 2024/2/29 20:00
 */
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
public class OrderOperationOption {
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
     * 完成订单操作
     */
    @Builder.Default
    Boolean confirm = Boolean.FALSE;
    /**
     * 退换退款操作
     */
    @Builder.Default
    Boolean returned = Boolean.FALSE;

    /**
     * 再次购买
     */
    @Builder.Default
    Boolean buy = Boolean.FALSE;

    /**
     * 修改地址-待发货才可以
     */
    @Builder.Default
    boolean updateAddress = Boolean.FALSE;
    /**
     * 打印面单(商家)
     */
    @Builder.Default
    Boolean print = Boolean.FALSE;

    /**
     * 备注
     */
    @Builder.Default
    Boolean mark = Boolean.FALSE;

    /**
     * 商家发货
     */
    @Builder.Default
    Boolean toShipping = Boolean.FALSE;

    /**
     * 查看物流
     */
    @Builder.Default
    Boolean viewLogistics = Boolean.FALSE;

    /**
     * 申请退款
     */
    @Builder.Default
    Boolean refundRequest = Boolean.FALSE;

    /**
     * 确认收货
     */
    @Builder.Default
    Boolean confirmReceipt = Boolean.FALSE;

    /**
     * 立即退款
     */
    @Builder.Default
    Boolean refundMoney = Boolean.FALSE;

    /**
     * 退款审核 退货申请通过后
     */
    @Builder.Default
    Boolean refundAudit = Boolean.FALSE;

    /**
     * 填写物流
     */
    @Builder.Default
    Boolean fillInLogistics = Boolean.FALSE;

    @Builder.Default
    Boolean cancelRefundRequest = Boolean.FALSE;


    /**
     * 买家可操作项
     *
     * @param status 订单状态
     * @return 买家可操作项
     */
    public Map<String, Boolean> buyerOption(Integer status) {
        OrderStatusEnum statusEnum = OrderStatusEnum.parse(status);
        switch (statusEnum) {
            case NEW: {
                // 待付款
                this.pay = Boolean.TRUE;
                this.updateAddress = Boolean.TRUE;
                this.cancel = Boolean.TRUE;
                break;
            }
            case PAID: {
                // 待发货,修改收件地址，查看物流，申请退款
                this.updateAddress = Boolean.TRUE;
                this.refundRequest = Boolean.TRUE;
                break;
            }
            case SHIPPED: {
                // 待收货 查看看物流，确认收货，申请退货退款
                this.viewLogistics = Boolean.TRUE;
                this.confirmReceipt = Boolean.TRUE;
                break;
            }
            case ROG: {
                // 已收货(确认收货) 查看物流、评价、开发票、申请退货退款
                this.refundRequest = Boolean.TRUE;
                this.comment = Boolean.TRUE;
                this.viewLogistics = Boolean.TRUE;
                break;
            }
            case CANCELED:
            case CLOSED:
            case COMPLETE: {
                // 交易关闭/完成/超时未支付关闭  可再次购买
                this.buy = Boolean.TRUE;
                break;
            }
        }
        return JSON.parseObject(JSON.toJSONString(this), new TypeReference<Map<String, Boolean>>() {
        });
    }


    public Map<String, Boolean> merchantOperation(Integer status) {
        OrderStatusEnum statusEnum = OrderStatusEnum.parse(status);
        switch (statusEnum) {
//            case AFTER_SERVICE: {
//                // 售后中,立即退款
//                this.refundMoney = Boolean.TRUE;
//                break;
//            }
            case PAID: {
                // 已付款,可发货
                this.toShipping = Boolean.TRUE;
                break;
            }
            case NEW: {
                // 商家在经用户同意后，主动取消
                this.cancel = Boolean.TRUE;
            }
        }
        return JSON.parseObject(JSON.toJSONString(this), new TypeReference<Map<String, Boolean>>() {
        });
    }
}
