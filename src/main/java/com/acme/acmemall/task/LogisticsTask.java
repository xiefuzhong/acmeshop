package com.acme.acmemall.task;

import com.acme.acmemall.kuaidi100.response.QueryTrackResp;
import com.acme.acmemall.kuaidi100.service.KuaiDi100QueryService;
import com.acme.acmemall.model.OrderDeliveryTrackVo;
import com.acme.acmemall.service.IOrderDeliveryTrackService;
import com.acme.acmemall.service.IOrderService;
import com.acme.acmemall.utils.DateUtils;
import com.acme.acmemall.utils.GsonUtil;
import com.acme.acmemall.utils.MapUtils;
import com.acme.acmemall.utils.StringUtils;
import com.google.common.base.Throwables;
import com.google.common.collect.Maps;
import org.apache.commons.collections.CollectionUtils;
import org.apache.log4j.Logger;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
@EnableScheduling
public class LogisticsTask {

    @Resource
    IOrderDeliveryTrackService deliveryTrackService;

    @Resource
    IOrderService orderService;

    @Resource
    KuaiDi100QueryService kuaiDi100QueryService;

    protected org.apache.log4j.Logger logger = Logger.getLogger(getClass());

    /**
     * 每1小时拉一次： 0 0 0/1 * * ?
     * 每45分钟：0 0/45 0 * * ?
     */
    @Async("asyncTaskExecutor")
    @Scheduled(fixedRate = 1980000)
    public void synDeliveryTrack() {
        //
        logger.info(String.format("同步物流踪迹，执行时间：%s", DateUtils.currentDate(DateUtils.DATE_TIME_PATTERN)));

        Map<String, Object> paramMap = Maps.newHashMap();
        // 查询已发货的（shipping_status = 1）的订单
        paramMap.put("shipping_status", 1);
        List<Map> resultList = deliveryTrackService.synDeliveryTrackList(paramMap);
        if (CollectionUtils.isEmpty(resultList)) {
            logger.info(String.format("查询数据，执行时间：%s,查无数据处理", DateUtils.currentDate(DateUtils.DATE_TIME_PATTERN)));
            return;
        }
        List firstResultList = resultList.stream().filter(item -> StringUtils.isNullOrEmpty(item.get("waybill_id").toString())).collect(Collectors.toList());
        if (CollectionUtils.isEmpty(firstResultList)) {
            logger.info(String.format("过滤数据，执行时间：%s,查无数据处理", DateUtils.currentDate(DateUtils.DATE_TIME_PATTERN)));
            return;
        }
        resultList.clear();
        // 收集ORDER_ID
        List paddingList = firstResultList;
        if (firstResultList.size() > 50) {
            paddingList = firstResultList.subList(0, 50);
            firstResultList.clear();
        }
        try {
//            List<OrderDeliveryTrackVo> deliveryTrackVoList = Lists.newArrayList();
            for (Object item : paddingList) {
                Map itemMap = (Map) item;
                Map<String, Object> params = Maps.newHashMap();
                params.put("com", MapUtils.getString("com", itemMap));
                params.put("num", MapUtils.getString("num", itemMap));
                QueryTrackResp trackResp = kuaiDi100QueryService.queryTrack(params);
                OrderDeliveryTrackVo deliveryTrackVo = OrderDeliveryTrackVo.builder()
                        .orderId(MapUtils.getString("order_id", itemMap))
                        .waybillId(trackResp.getNu())
                        .latestInfo(GsonUtil.toJson(trackResp.getData().stream().findFirst().get()))
                        .allInfo(GsonUtil.toJson(trackResp.getData()))
                        .build();
//                deliveryTrackVoList.add(deliveryTrackVo);
                deliveryTrackService.saveDeliveryTrack(deliveryTrackVo);
            }

        } catch (Exception e) {
            logger.error("同步物流信息出错了" + Throwables.getStackTraceAsString(e));
        }
    }

    /**
     * 1分钟60s
     * fixedRate:毫秒
     */
    @Scheduled(fixedRate = 300000)
    public void test() {
        logger.info(String.format("test-- 执行时间：%s", DateUtils.currentDate(DateUtils.DATE_TIME_PATTERN)));
    }
}
