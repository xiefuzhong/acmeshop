package com.acme.acmemall.service;

import com.acme.acmemall.model.InvoiceTitleVo;

import java.util.List;
import java.util.Map;

public interface IInvoiceTitleService {
    List<InvoiceTitleVo> queryInvoiceTitleList(Map<String, Object> param);

    void save(InvoiceTitleVo titleVo);

    InvoiceTitleVo queryObject(Integer id);

    InvoiceTitleVo queryDefaultByUserId(long userId);

    void update(InvoiceTitleVo entity);

    void delete(Integer id);

    void updateIsDefault(InvoiceTitleVo entity);
}
