package com.acme.acmemall.service;

import com.acme.acmemall.model.GoodsGalleryVo;

import java.util.List;
import java.util.Map;

public interface IGoodsGalleryService {

    List<GoodsGalleryVo> queryGoodsGalleryList(Map<String,Object> map);
}
