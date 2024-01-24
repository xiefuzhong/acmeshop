package com.acme.acmemall.service;

import com.acme.acmemall.model.OrderDeliveryTrackVo;

import java.util.List;
import java.util.Map;

public interface IOrderDeliveryTrackService {

    List<Map> synDeliveryTrackList(Map params);

    void saveDeliveryTrack(OrderDeliveryTrackVo deliveryTrackVo);
}
