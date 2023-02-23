package com.acme.acmemall.controller;

import com.acme.acmemall.annotation.LoginUser;
import com.acme.acmemall.controller.reqeust.OrderSubmitRequest;
import com.acme.acmemall.model.LoginUserVo;
import com.acme.acmemall.service.IOrderService;
import com.alibaba.fastjson.JSONObject;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.collections.MapUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * @description:订单相关
 * @author: ihpangzi
 * @time: 2023/2/17 15:53
 */
@Api(tags = "订单相关")
@RestController
@RequestMapping("/api/order")
public class OrderController extends ApiBase {

    @Autowired
    IOrderService orderService;

    @ApiOperation(value = "订单提交")
    @PostMapping("submit")
    public Object submit(@LoginUser LoginUserVo loginUser) {
        Map resultObj = null;
        try {
            OrderSubmitRequest request = JSONObject.toJavaObject(getJsonRequest(), OrderSubmitRequest.class);
            request.check();
            resultObj = orderService.submit(request, loginUser);
            if (null != resultObj) {
                return toResponsObject(MapUtils.getInteger(resultObj, "errno"), MapUtils.getString(resultObj, "errmsg"), resultObj.get("data"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return toResponsFail("提交失败");
    }
}
