package com.acme.acmemall.dao;

import com.acme.acmemall.model.OrderVo;
import com.acme.acmemall.model.StatisticsVo;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface OrderMapper extends BaseDao<OrderVo> {
    void updateStatus(OrderVo newOrder);

    List<StatisticsVo> statistics(Map<String, Object> params);

    List<OrderVo> queryMerOrders(Map<String, Object> params);

    List<OrderVo> queryListByIds(List<String> orderIds);

    List<OrderVo> queryPendingDataByTask(Map<String, Object> params);

    int cancelBatch(List<String> orderIds);

    /**
     * 批量更新<br>
     * 更新状态，操作时间(取消时间、支付时间、发货时间、收货时间，评价时间、售后时间)
     *
     * @param entityList
     * @return
     */
    int batchUpdate(List<OrderVo> entityList);
}
