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

    void batchUpdate(List<OrderVo> entityList);
}
