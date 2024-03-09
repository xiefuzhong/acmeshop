package com.acme.acmemall.controller;

import com.acme.acmemall.annotation.LoginUser;
import com.acme.acmemall.common.ResultMap;
import com.acme.acmemall.controller.reqeust.OrderRefundRequest;
import com.acme.acmemall.exception.Assert;
import com.acme.acmemall.model.LoginUserVo;
import com.acme.acmemall.service.IOrderRefundService;
import com.acme.acmemall.service.IUserService;
import com.alibaba.fastjson.JSONObject;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * @description:
 * @author: ihpangzi
 * @time: 2024/2/28 17:43
 */
@RestController
@RequestMapping("/api/order-refund")
public class OrderRefundController extends ApiBase {

    @Resource
    IUserService userService;

    @Resource
    IOrderRefundService refundService;

    @PostMapping("/post")
    public Object refundPost(@LoginUser LoginUserVo userVo) {
        if (userVo == null) {
            return ResultMap.error(400, "非有效用户操作");
        }
        JSONObject object = getJsonRequest();
        if (object == null) {
            return ResultMap.badArgument();
        }
        OrderRefundRequest request = JSONObject.toJavaObject(object, OrderRefundRequest.class);
        request.check();
        return refundService.submit(request, userVo);
    }

    @PostMapping("/update")
    public Object refundUpdate(@LoginUser LoginUserVo userVo) {
        if (userVo == null) {
            return ResultMap.error(400, "非有效用户操作");
        }
        JSONObject object = getJsonRequest();
        if (object == null) {
            return ResultMap.badArgument();
        }
        OrderRefundRequest request = JSONObject.toJavaObject(object, OrderRefundRequest.class);
        logger.info("refundUpdate>>" + request.toString());
        Assert.isNull(request.getOrderId(), "订单号不能为空!");
        return refundService.updateRefund(request);
    }
}
