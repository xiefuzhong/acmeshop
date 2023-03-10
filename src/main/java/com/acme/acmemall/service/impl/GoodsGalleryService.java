package com.acme.acmemall.service.impl;

import com.acme.acmemall.dao.GoodsGalleryMapper;
import com.acme.acmemall.model.GoodsGalleryVo;
import com.acme.acmemall.service.IGoodsGalleryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * @description:商品橱窗展示信息
 * @author: ihpangzi
 * @time: 2023/2/14 8:55
 */
@Service
public class GoodsGalleryService implements IGoodsGalleryService {
    @Autowired
    private GoodsGalleryMapper mapper;
    /**
     * @param map
     * @return
     */
    @Override
    public List<GoodsGalleryVo> queryGoodsGalleryList(Map<String, Object> map) {
        return mapper.queryList(map);
    }
}
