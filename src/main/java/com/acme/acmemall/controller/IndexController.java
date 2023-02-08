package com.acme.acmemall.controller;

import com.acme.acmemall.annotation.IgnoreAuth;
import com.acme.acmemall.model.AdVo;
import com.acme.acmemall.service.IAdService;
import com.google.common.collect.Maps;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

/**
 * 首页入口
 * @author IHPANGZI
 * @date 2023-02-08
 */
@Api(tags = "商城首页")
@RestController
@RequestMapping("/api/index")
public class IndexController extends ApiBase {
  @Autowired
  private IAdService adService;

  /**
   * 主页页面
   * @return API response html
   */
  @GetMapping
  public String index() {
    return "index";
  }

  @ApiOperation(value = "banner")
  @IgnoreAuth
  @GetMapping(value = "banner")
  public Object banner() {
    Map<String, Object> resultObj = Maps.newHashMap();
    Map<String, Object> param = Maps.newHashMap();
    param.put("ad_position_id", 1);
    List<AdVo> banner = adService.queryAdList(param);
    resultObj.put("banner", banner);
    return toResponsSuccess(resultObj);
  }

}
