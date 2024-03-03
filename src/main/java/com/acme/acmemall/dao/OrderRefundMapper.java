package com.acme.acmemall.dao;

import com.acme.acmemall.model.OrderRefundVo;
import org.apache.ibatis.annotations.Mapper;

/**
 * @description:
 * @author: ihpangzi
 * @time: 2024/2/28 20:37
 */
@Mapper
public interface OrderRefundMapper extends BaseDao<OrderRefundVo> {

    OrderRefundVo findByOrderId(String orderId);
}
