package com.acme.acmemall.service.impl;

import com.acme.acmemall.model.ChannelVo;
import com.acme.acmemall.service.IChannelService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class ChannelService implements IChannelService {

    /**
     * @param map
     * @return
     */
    @Override
    public List<ChannelVo> queryChannelList(Map<String, Object> map) {
        return null;
    }
}
