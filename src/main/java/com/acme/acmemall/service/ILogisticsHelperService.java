package com.acme.acmemall.service;

import com.acme.acmemall.model.LogisticsOrder;

public interface ILogisticsHelperService {

    Object addOrder(LogisticsOrder order, long userId);
}
