package com.acme.acmemall.service.impl;

import com.acme.acmemall.dao.OrderDeliveryTrackMapper;
import com.acme.acmemall.service.IOrderDeliveryTrackService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * @description:
 * @author: ihpangzi
 * @time: 2024/1/24 15:40
 */
@Service
public class OrderDeliveryTrackServiceImpl implements IOrderDeliveryTrackService {

    @Resource
    OrderDeliveryTrackMapper deliveryTrackMapper;

    /**
     * @param params
     * @return
     */
    @Override
    public List<Map> synDeliveryTrackList(Map params) {
        return deliveryTrackMapper.queryExistTrack(params);
    }
}
