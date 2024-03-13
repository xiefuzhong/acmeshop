package com.acme.acmemall.controller;

import com.acme.acmemall.annotation.LoginUser;
import com.acme.acmemall.common.ResultMap;
import com.acme.acmemall.controller.reqeust.OrderRefundRequest;
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
        request.reqCheck();
        logger.info("refundPost.request==>" + request);
        return refundService.submit(request, userVo);
    }

    /**
     * 售后操作：
     * 买家：申请退款(未发货)，申请退货退款(确认收货/拒收)，退还商品(填写物流信息)
     * 卖家：审批(同意退款、拒绝退款) 确认收货(退还商品)，退款仅支持原路退回(不支持账号退回，填写账号和金额)
     *
     * @param userVo
     * @return
     */
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
        request.updateCheck();
        return refundService.updateRefund(request);
    }
}
