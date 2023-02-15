package com.acme.acmemall.service.impl;

import com.acme.acmemall.model.AddressVo;
import com.acme.acmemall.service.IAddressService;
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
    /**
     * 收件地址
     *
     * @param id
     * @return
     */
    @Override
    public AddressVo queryObject(Integer id) {
        return null;
    }

    /**
     * 收件地址查询
     *
     * @param map
     * @return
     */
    @Override
    public List<AddressVo> queryList(Map<String, Object> map) {
        return null;
    }

    /**
     * 用户默认地址
     *
     * @param userId
     * @return
     */
    @Override
    public AddressVo queryDefaultAddress(Long userId) {
        return null;
    }
}
