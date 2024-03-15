package com.acme.acmemall.service;

import com.acme.acmemall.common.ResultMap;
import com.acme.acmemall.controller.reqeust.OrderSubmitRequest;
import com.acme.acmemall.model.LoginUserVo;
import com.acme.acmemall.model.OrderVo;

import java.util.List;
import java.util.Map;

/**
 * @description:购物车功能
 * @author: ihpangzi
 * @time: 2023/2/13 11:20
 */
public interface IOrderService {
    /**
     * 订单提交
     *
     * @param request   订单提交请求参数
     * @param loginUser 登录用户信息
     * @return 订单提交结果
     */
    ResultMap submit(OrderSubmitRequest request, LoginUserVo loginUser);

    List<OrderVo> queryOrderList(Map params);

    /**
     * 订单详情查询
     *
     * @param orderId
     * @return 订单详情
     */
    OrderVo findOrder(String orderId);

    void updateOrder(OrderVo orderVo);

    void updateStatus(OrderVo newOrder);

    /**
     * 商户管理订单
     *
     * @param orderVo
     */
    void handleOrderByMer(OrderVo orderVo);

    List<OrderVo> queryByIds(List<String> ids);

    /**
     * 查询待处理的数据列表
     *
     * @param params
     * @return
     */
    List<OrderVo> queryPendingDataByTask(Map params);

    /**
     * 根据订单ID更新状态
     *
     * @param orderIds
     */
    int updateByIds(List<String> orderIds);

    /**
     * 批量更新
     *
     * @param entityList
     */
    void batchUpdate(List<OrderVo> entityList);
}
