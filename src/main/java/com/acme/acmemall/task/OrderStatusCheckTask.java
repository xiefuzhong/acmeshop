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
import java.util.stream.Collectors;

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
     * 订单半个小时未付款要自动取消
     * fixedRate:毫秒
     */
    @Scheduled(fixedRate = AUTO_CANCEL_RATE)
    public void autoCancelOrderTask() {
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
            List<String> ids = orders.stream().map(OrderVo::getId).collect(Collectors.toList());
            int result = orderService.updateByIds(ids);
            logger.info(String.format("处理数据条数：%s", result));
        } catch (Exception e) {
            logger.error(String.format("autoCancelOrderTask-- Exception：%s", Throwables.getStackTraceAsString(e)));
        }
    }

    // 取消的订单 自动完结(5分钟)。
//    @Scheduled(fixedRate = AUTO_END_RATE)
    public void autoEnd() {

    }

    @Scheduled(fixedRate = AUTO_TIMEOUT)
    public void timeout() {
        logger.info(String.format("timeout-- 执行开始时间：%s", DateUtils.currentDate(DateUtils.DATE_TIME_PATTERN)));
        try {

        } catch (Exception e) {
            logger.error(String.format("timeout-- Exception：%s", Throwables.getStackTraceAsString(e)));
        }
        logger.info(String.format("timeout-- 执行结束时间：%s", DateUtils.currentDate(DateUtils.DATE_TIME_PATTERN)));
    }
}
