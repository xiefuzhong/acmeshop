package com.acme.acmemall.controller;

import com.acme.acmemall.annotation.IgnoreAuth;
import com.acme.acmemall.model.RegionVo;
import com.acme.acmemall.service.ISysRegionService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @description:基础数据查询
 * @author: ihpangzi
 * @time: 2023/2/18 16:38
 */
@Api(tags = "基础数据")
@RestController
@RequestMapping("/api/basic-data")
public class RegionController extends ApiBase {

    @Autowired
    ISysRegionService regionService;

    @ApiOperation(value = "地区列表")
    @IgnoreAuth
    @GetMapping("region-list")
    public Object list(Integer parentId) {
        List<RegionVo> regionVoList = regionService.getChildrenByParentId(parentId);
        return toResponsSuccess(regionVoList);
    }
}
