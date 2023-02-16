package com.acme.acmemall.service.impl;

import com.acme.acmemall.dao.AddressMapper;
import com.acme.acmemall.model.AddressVo;
import com.acme.acmemall.service.IAddressService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * @description:
 * @author: ihpangzi
 * @time: 2023/2/15 14:47
 */
@Service
public class AddressService implements IAddressService {

    @Autowired
    AddressMapper mapper;

    /**
     * 收件地址
     *
     * @param id
     * @return
     */
    @Override
    public AddressVo queryObject(Integer id) {
        return mapper.queryObject(id);
    }

    /**
     * 收件地址查询
     *
     * @param map
     * @return
     */
    @Override
    public List<AddressVo> queryAddressList(Map<String, Object> map) {
        return mapper.queryList(map);
    }

    /**
     * 用户默认地址
     *
     * @param userId
     * @return
     */
    @Override
    public AddressVo queryDefaultAddress(Long userId) {
        return mapper.queryDefaultAddress(userId);
    }

    /**
     * @param address
     */
    @Override
    public void updateIsDefault(AddressVo address) {
        mapper.updateIsDefault(address);
    }

    /**
     * @param entity
     */
    @Override
    public void save(AddressVo entity) {
        mapper.save(entity);
    }

    /**
     * @param entity
     */
    @Override
    public void update(AddressVo entity) {
        mapper.update(entity);
    }

    /**
     * @param id
     */
    @Override
    public void delete(Integer id) {
        mapper.delete(id);
    }

    /**
     * @param param
     * @return
     */
    @Override
    public List<AddressVo> queryaddressUserlist(Map<String, Object> param) {
        return mapper.queryAddressCustomerlist(param);
    }
}
