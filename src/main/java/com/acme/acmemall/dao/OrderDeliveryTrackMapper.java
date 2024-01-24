package com.acme.acmemall.dao;

import com.acme.acmemall.model.OrderDeliveryTrackVo;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface OrderDeliveryTrackMapper extends BaseDao<OrderDeliveryTrackVo> {

    List<Map> queryExistTrack(Map params);
}
