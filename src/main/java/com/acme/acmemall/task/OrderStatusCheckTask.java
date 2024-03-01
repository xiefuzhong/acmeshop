package com.acme.acmemall.task;

import com.acme.acmemall.utils.DateUtils;
import com.google.common.base.Throwables;
import org.apache.log4j.Logger;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * @description:
 * @author: ihpangzi
 * @time: 2024/3/1 16:31
 */
@Component
@EnableScheduling
public class OrderStatusCheckTask {

    protected final Logger logger = Logger.getLogger(this.getClass());

    /**
     * 订单24小时未付款要自动取消
     * fixedRate:毫秒
     */
    @Scheduled(fixedRate = 60 * 60 * 1000)
    public void autoCancelOrderTask() {
        logger.info(String.format("autoCancelOrderTask-- 执行时间：%s", DateUtils.currentDate(DateUtils.DATE_TIME_PATTERN)));
        try {

        } catch (Exception e) {
            logger.error(Throwables.getStackTraceAsString(e));
        }
    }
}
