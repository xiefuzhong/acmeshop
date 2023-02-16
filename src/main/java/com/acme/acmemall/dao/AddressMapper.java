package com.acme.acmemall.dao;

import com.acme.acmemall.model.AddressVo;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

/**
 * @description:
 * @author: ihpangzi
 * @time: 2023/2/16 14:20
 */
@Mapper
public interface AddressMapper extends BaseDao<AddressVo>{
    void updateIsDefault(AddressVo addressVo);

    List<AddressVo> queryaddressUserlist(Map<String, Object> param);

    List<AddressVo> queryAddressCustomerlist(Map<String, Object> param);

    AddressVo queryDefaultAddress(Long userId);
}
