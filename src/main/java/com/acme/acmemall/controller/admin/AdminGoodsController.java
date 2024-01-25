package com.acme.acmemall.controller.admin;

import com.acme.acmemall.annotation.LoginUser;
import com.acme.acmemall.common.ResultMap;
import com.acme.acmemall.controller.ApiBase;
import com.acme.acmemall.model.LoginUserVo;
import com.acme.acmemall.model.SpecificationVo;
import com.acme.acmemall.service.ISpecificationService;
import com.acme.acmemall.service.IUserService;
import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Maps;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * @description:
 * @author: ihpangzi
 * @time: 2024/1/25 11:27
 */
@RestController
@RequestMapping("/api/admin")
public class AdminGoodsController extends ApiBase {

    @Resource
    private ISpecificationService specService;

    private IUserService userService;

    @GetMapping("/spec-list")
    public Object queryByMerchantId(@LoginUser LoginUserVo userVo) {
        if (userVo == null || userVo.getUserId() == null) {
            return ResultMap.badArgument("未登录用户操作");
        }
        LoginUserVo loginUserVo = userService.queryByUserId(userVo.getUserId());
        if (userVo == null || userVo.getUserId() == 0) {
            return ResultMap.error(1001, "请先登录管理系统再操作!");
        }
        Map paramMap = Maps.newHashMap();
        paramMap.put("merchantId", loginUserVo.getMerchantId());
        List<SpecificationVo> specList = specService.querySpecifications(paramMap);
        return toResponsSuccess(specList);
    }

    @PostMapping("/spec-add")
    public Object saveSpecification(@LoginUser LoginUserVo userVo) {
        JSONObject jsonObject = super.getJsonRequest();
        if (jsonObject == null) {
            return ResultMap.badArgument();
        }
        SpecificationVo specVo = JSONObject.toJavaObject(jsonObject, SpecificationVo.class);
        specVo.setMerchantId(userVo.getMerchantId());
        specService.addSpecification(specVo);
        return toResponsSuccess(null);
    }

}
