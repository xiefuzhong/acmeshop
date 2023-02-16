package com.acme.acmemall.service;

import com.acme.acmemall.model.AddressVo;

import java.util.List;
import java.util.Map;

public interface IAddressService {

    /**
     * 收件地址
     * @param id
     * @return
     */
    AddressVo queryObject(Integer id);

    /**
     * 收件地址查询
     * @param map
     * @return
     */
    List<AddressVo> queryAddressList(Map<String, Object> map);

    /**
     * 用户默认地址
     * @param userId
     * @return
     */
    AddressVo queryDefaultAddress(Long userId);

    void updateIsDefault(AddressVo entity1);

    void save(AddressVo entity);

    void update(AddressVo entity);

    void delete(Integer id);

    List<AddressVo> queryaddressUserlist(Map<String, Object> param);
}
