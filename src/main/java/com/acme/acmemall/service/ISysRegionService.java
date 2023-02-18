package com.acme.acmemall.service;

import com.acme.acmemall.model.RegionVo;

import java.util.List;

public interface ISysRegionService {
    List<RegionVo> getChildrenByParentId(Integer parentId);

}
