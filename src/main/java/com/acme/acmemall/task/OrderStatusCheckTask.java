package com.acme.acmemall.task;

import com.acme.acmemall.model.OrderVo;
import com.acme.acmemall.model.enums.OrderStatusEnum;
import com.acme.acmemall.model.enums.PayStatusEnum;
import com.acme.acmemall.service.IOrderService;
import com.acme.acmemall.utils.DateUtils;
import com.google.common.base.Throwables;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.apache.commons.collections.CollectionUtils;
import org.apache.log4j.Logger;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * @description:
 * @author: ihpangzi
 * @time: 2024/3/1 16:31
 */
@Component
@EnableScheduling
public class OrderStatusCheckTask {

    @Resource
    IOrderService orderService;

    protected final Logger logger = Logger.getLogger(this.getClass());


    /**
     * 订单半个小时未付款要自动取消
     */
    private static final long AUTO_CANCEL_RATE = 1800000;

    /**
     * 取消的订单 自动完结(5分钟)。
     */
    private static final long AUTO_END_RATE = 300000;

    /**
     * 超时(超时自动好评,售后超出时效,超时未支付自动关闭)
     */
    private static final long AUTO_TIMEOUT = 300000;

    /**
     * 支付超时自动取消:超下订单时间半个小时自动取消订单<br>
     * fixedRate:毫秒
     */
    @Scheduled(fixedRate = AUTO_CANCEL_RATE)
    public void payTimeout() {
        logger.info(String.format("autoCancelOrderTask-- 执行时间：%s", DateUtils.currentDate(DateUtils.DATE_TIME_PATTERN)));
        try {
            Map param = Maps.newHashMap();
            List<Integer> status = Lists.newArrayList(OrderStatusEnum.NEW.getCode());
            param.put("status", status);
            param.put("pay_status", PayStatusEnum.PAY_NO.getCode());
            param.put("data_type", "toCancel");
            List<OrderVo> orders = orderService.queryPendingDataByTask(param);

            int size = CollectionUtils.isEmpty(orders) ? 0 : orders.size();
            logger.info(String.format("查询出待处理数据条数：%s", size));
            if (size == 0) {
                return;
            }
            orders.stream().forEach(order -> order.cancle("支付超时自动取消"));
            orderService.batchUpdate(orders);
            logger.info(String.format("处理数据条数：%s", 0));
        } catch (Exception e) {
            logger.error(String.format("autoCancelOrderTask-- Exception：%s", Throwables.getStackTraceAsString(e)));
        }
    }

    // 取消的订单 自动完结(5分钟)。
    @Scheduled(fixedRate = AUTO_END_RATE)
    public void autoEnd() {
        logger.info(String.format("autoEnd-- 执行开始时间：%s", DateUtils.currentDate(DateUtils.DATE_TIME_PATTERN)));
        try {
            Map params = Maps.newHashMap();
            // 已取消、已退款,已退货退款
            List<Integer> status = Lists.newArrayList(OrderStatusEnum.CANCELED.getCode(),
                    OrderStatusEnum.REFUNDED.getCode(),
                    OrderStatusEnum.REFUND_RETURNED.getCode());
            params.put("status", status);
            params.put("data_type", "toEnd");
            List<OrderVo> orders = orderService.queryPendingDataByTask(params);
            int size = CollectionUtils.isEmpty(orders) ? 0 : orders.size();
            logger.info(String.format("查询出待处理数据条数：%s", size));
            if (size == 0) {
                return;
            }
            orders.stream().forEach(orderVo -> orderVo.autoEnd());
            orderService.batchUpdate(orders);
        } catch (Exception e) {
            logger.error(String.format("autoEnd-- Exception：%s", Throwables.getStackTraceAsString(e)));
        }
        logger.info(String.format("autoEnd-- 执行结束时间：%s", DateUtils.currentDate(DateUtils.DATE_TIME_PATTERN)));
    }

    /**
     * 评价超时：自收货之日起，15天内未评价的订单自动好评<br>
     * 自动确认收货：自发货之日起，10天内未确认收货的订单自动确认收货;<br>
     * 售后失效：自发货之日起，30天后不允许发起售后;<br>
     * 自动退款：仅退款且审批通过2天内未退款的订单自动退款，退货退款且确认退回的2天内未退款的订单自动退款<br>
     */
    @Scheduled(fixedRate = AUTO_TIMEOUT)
    public void timeout() {
        logger.info(String.format("timeout-- 执行开始时间：%s", DateUtils.currentDate(DateUtils.DATE_TIME_PATTERN)));
        try {
            Map params = Maps.newHashMap();
            params.put("data_type", "toTimeout");
        } catch (Exception e) {
            logger.error(String.format("timeout-- Exception：%s", Throwables.getStackTraceAsString(e)));
        }
        logger.info(String.format("timeout-- 执行结束时间：%s", DateUtils.currentDate(DateUtils.DATE_TIME_PATTERN)));
    }
}
