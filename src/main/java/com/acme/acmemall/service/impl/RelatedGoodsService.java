package com.acme.acmemall.service.impl;

import com.acme.acmemall.dao.RelatedGoodsMapper;
import com.acme.acmemall.model.RelatedGoodsVo;
import com.acme.acmemall.service.IRelatedGoodsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * @description:
 * @author: ihpangzi
 * @time: 2023/12/30 16:18
 */
@Service
public class RelatedGoodsService implements IRelatedGoodsService {
    @Autowired
    private RelatedGoodsMapper relatedGoodsDao;

    /**
     * @param id
     * @return
     */
    @Override
    public RelatedGoodsVo queryObject(Integer id) {
        return relatedGoodsDao.queryObject(id);
    }

    /**
     * @param paramMap
     * @return
     */
    @Override
    public List<RelatedGoodsVo> queryList(Map<String, Object> paramMap) {
        return relatedGoodsDao.queryList(paramMap);
    }
}
