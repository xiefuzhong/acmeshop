package com.acme.acmemall.controller;

import com.acme.acmemall.annotation.IgnoreAuth;
import com.acme.acmemall.annotation.LoginUser;
import com.acme.acmemall.model.AddressVo;
import com.acme.acmemall.model.LoginUserVo;
import com.acme.acmemall.service.IAddressService;
import com.acme.acmemall.service.IUserService;
import com.acme.acmemall.utils.GsonUtil;
import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Maps;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @description:收货地址
 * @author: ihpangzi
 * @time: 2023/2/16 14:18
 */
@Api(tags = "收货地址")
@RestController
@RequestMapping("/api/address")
public class AddressController extends ApiBase {
    @Autowired
    IAddressService addressService;

    @Resource
    IUserService userService;

    /**
     * 获取用户的收货地址
     */
    @ApiOperation(value = "获取用户的收货地址接口", response = Map.class)
    @GetMapping("list")
    public Object list(@LoginUser LoginUserVo loginUser) {
        Map<String, Object> param = new HashMap<String, Object>();
        param.put("user_id", loginUser.getUserId());
        List<AddressVo> addressList = addressService.queryAddressList(param);
        return toResponsSuccess(addressList);
    }

    /**
     * 获取收货地址的详情
     */
    @ApiOperation(value = "获取收货地址的详情", response = Map.class)
    @ApiImplicitParams({@ApiImplicitParam(name = "id", value = "收获地址ID", required = true, dataType = "Integer")})
    @GetMapping("detail")
    public Object detail(Integer id, @LoginUser LoginUserVo loginUser) {
        AddressVo entity = addressService.queryObject(id);
        if (entity == null) {
            return toResponsSuccess(null);
        }
        return toResponsSuccess(entity);
    }

    /**
     * 添加或更新收货地址
     */
    @ApiOperation(value = "添加或更新收货地址", response = Map.class)
    @PostMapping("save")
    public Object save(@LoginUser LoginUserVo loginUser) {
        JSONObject addressJson = this.getJsonRequest();
        AddressVo entity = new AddressVo();
        Long userId = addressJson.containsKey("merchantId") ? addressJson.getLong("merchantId") : loginUser.getUserId();
        if (null != addressJson) {
            entity.setId(addressJson.getLong("id"));
            entity.setType(addressJson.getInteger("type"));
            entity.setUserId(userId);
            entity.setUserName(addressJson.getString("userName"));
            entity.setPostalCode(addressJson.getString("postalCode"));
            entity.setProvinceName(addressJson.getString("provinceName"));
            entity.setCityName(addressJson.getString("cityName"));
            entity.setCountyName(addressJson.getString("countyName"));
            entity.setDetailInfo(addressJson.getString("detailInfo"));
            entity.setNationalCode(addressJson.getString("nationalCode"));
            entity.setTelNumber(addressJson.getString("telNumber"));
            entity.setIs_default(addressJson.getInteger("is_default"));
        }
        Map<String, Object> param = Maps.newHashMap();
        param.put("user_id", userId);
        List<AddressVo> addressEntities = addressService.queryAddressList(param);
        if (addressEntities.size() == 0) {//第一次添加设置为默认地址
            entity.setIs_default(1);
        }
        //设置默认地址
        if (entity.getIs_default() == 1) {
            AddressVo entity1 = new AddressVo();
            entity1.setUserId(loginUser.getUserId());
            entity1.setIs_default(0);
            addressService.updateIsDefault(entity1);
        }
        if (null == entity.getId() || entity.getId() == 0) {
            entity.setId(null);
            addressService.save(entity);
        } else {
            addressService.update(entity);
        }
        return toResponsSuccess(entity);
    }

    /**
     * 删除指定的收货地址
     */
    @ApiOperation(value = "删除指定的收货地址", response = Map.class)
    @PostMapping("delete")
    public Object delete(@LoginUser LoginUserVo loginUser) {
        JSONObject jsonParam = this.getJsonRequest();
        Integer id = jsonParam.getIntValue("id");

        AddressVo entity = addressService.queryObject(id);
        //判断越权行为
        if (!userService.checkAdmin(loginUser.getUserId())) {
            return toResponsFail("您不是管理员");
        }
        addressService.delete(id);
        return toResponsSuccess("");
    }

    /**
     * 获取用户的收货地址(包含商家的发货/退货地址)
     */
    @IgnoreAuth
    @ApiOperation(value = "获取用户的收货地址接口", response = Map.class)
    @GetMapping("addressUserlist")
    public Object addressUserlist(@LoginUser LoginUserVo loginUser,
                                  @RequestParam("user_id") Integer user_id,
                                  @RequestParam("type") Integer type) {
        Map<String, Object> param = Maps.newHashMap();
        if (user_id != null) {
            param.put("user_id", user_id);
        } else {
            param.put("user_id", this.getUserId().intValue());
            param.put("type", 0);
        }
        if (type != null) {
            param.put("type", type);
        }
        logger.info("addressUserlist" + GsonUtil.toJson(param));
        List<AddressVo> addressEntities = addressService.queryaddressUserlist(param);
        return toResponsSuccess(addressEntities);
    }
}
