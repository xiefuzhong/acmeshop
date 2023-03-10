package com.acme.acmemall.service.impl;

import com.acme.acmemall.dao.CollectMapper;
import com.acme.acmemall.model.CollectVo;
import com.acme.acmemall.service.ICollectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * @description:
 * @author: ihpangzi
 * @time: 2023/2/14 10:20
 */
@Service
public class CollectService implements ICollectService {

    @Autowired
    CollectMapper collectMapper;

    /**
     * @param map
     * @return
     */
    @Override
    public Integer queryTotal(Map<String, Object> map) {
        return collectMapper.queryTotal(map);
    }

    /**
     * @param param
     * @return
     */
    @Override
    public List<CollectVo> queryList(Map param) {
        return collectMapper.queryList(param);
    }

    /**
     * @param collectEntity
     * @return
     */
    @Override
    public Integer save(CollectVo collectEntity) {
        return collectMapper.save(collectEntity);
    }

    /**
     * @param id
     * @return
     */
    @Override
    public Integer delete(Integer id) {
        return collectMapper.delete(id);
    }

    /**
     * @param collect
     * @return
     */
    @Override
    public Integer saveOrUpdate(CollectVo collect) {
        if (collect.getIs_attention() == 0) {
            return collectMapper.save(collect);
        }
        return collectMapper.delete(collect.getId());
    }
}
