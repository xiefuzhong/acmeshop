package com.acme.acmemall.service;

import com.acme.acmemall.model.ChannelVo;

import java.util.List;
import java.util.Map;

public interface IChannelService {
    List<ChannelVo> queryChannelList(Map<String, Object> map);

}
