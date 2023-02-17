package com.acme.acmemall.service.impl;

import com.acme.acmemall.model.LoginUserVo;
import com.acme.acmemall.service.IOrderService;
import com.alibaba.fastjson.JSONObject;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * @description:
 * @author: ihpangzi
 * @time: 2023/2/17 15:58
 */
@Service
public class OrderService implements IOrderService {
    protected Logger logger = Logger.getLogger(getClass());

    /**
     * 订单提交
     *
     * @param jsonRequest 请求参数
     * @param loginUser   登录用户信息
     * @return 订单提交结果
     */
    @Override
    public Map submit(JSONObject jsonRequest, LoginUserVo loginUser) {
        logger.info("order.submit--> "+jsonRequest.toString());
        return null;
    }
}
