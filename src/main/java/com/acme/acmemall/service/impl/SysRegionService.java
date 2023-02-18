package com.acme.acmemall.service.impl;

import com.acme.acmemall.dao.SysRegionMapper;
import com.acme.acmemall.model.RegionVo;
import com.acme.acmemall.service.ISysRegionService;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.apache.commons.collections.CollectionUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @description:
 * @author: ihpangzi
 * @time: 2023/2/18 17:04
 */
@Service
public class SysRegionService implements ISysRegionService {

    public static List<RegionVo> regionList;
    protected Logger logger = Logger.getLogger(getClass());
    @Autowired
    SysRegionMapper mapper;

    @PostConstruct
    public void init() {
        logger.info("<--region.init()-->");
        if (null != regionList) {
            regionList = mapper.queryList(Maps.newHashMap());
        }
    }

    /**
     * 省市区县列表
     * @param parentId 层 0-省级
     * @return
     */
    @Override
    public List<RegionVo> getChildrenByParentId(Integer parentId) {
        logger.info("region.getChildrenByParentId>>");
        if (parentId == null) {
            return Lists.newArrayList();
        }
        if (CollectionUtils.isEmpty(regionList)) {
            return Lists.newArrayList();
        }
        return regionList.stream().filter(regionVo -> (regionVo.getParent_id() != null && regionVo.getParent_id() == parentId)).collect(Collectors.toList());
    }
}
