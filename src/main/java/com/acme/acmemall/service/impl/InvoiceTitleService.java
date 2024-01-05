package com.acme.acmemall.service.impl;

import com.acme.acmemall.dao.InvoiceTitleMapper;
import com.acme.acmemall.model.InvoiceTitleVo;
import com.acme.acmemall.service.IInvoiceTitleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * @description:
 * @author: ihpangzi
 * @time: 2024/1/4 16:55
 */
@Service
public class InvoiceTitleService implements IInvoiceTitleService {
    @Autowired
    private InvoiceTitleMapper InvoiceTitleDao;

    /**
     * @param param
     * @return
     */
    @Override
    public List<InvoiceTitleVo> queryInvoiceTitleList(Map<String, Object> param) {
        return InvoiceTitleDao.queryList(param);
    }

    public void save(InvoiceTitleVo titleVo) {
        InvoiceTitleDao.save(titleVo);
    }

    /**
     * @param id
     * @return
     */
    @Override
    public InvoiceTitleVo queryObject(Integer id) {
        return InvoiceTitleDao.queryObject(id);
    }

    /**
     * @param userId
     * @return
     */
    @Override
    public InvoiceTitleVo queryDefaultByUserId(long userId) {
        return InvoiceTitleDao.queryDefaultByUserId(userId);
    }

    /**
     * @param entity
     */
    @Override
    public void update(InvoiceTitleVo entity) {
        InvoiceTitleDao.update(entity);
    }

    /**
     * @param id
     */
    @Override
    public void delete(Integer id) {
        InvoiceTitleDao.delete(id);
    }

    /**
     * @param entity
     */
    @Override
    public void updateIsDefault(InvoiceTitleVo entity) {
        InvoiceTitleDao.updateIsDefault(entity);
    }
}
