package com.acme.acmemall.model;

import lombok.Data;
import org.apache.commons.lang.StringUtils;

import java.io.Serializable;

/**
 * @description:收货地址
 * @author: ihpangzi
 * @time: 2023/2/15 14:23
 */
@Data
public class AddressVo implements Serializable {
    //主键
    private Long id;
    //会员ID
    private Long userId;
    //收货人姓名
    private String userName;
    //手机
    private String telNumber;
    //邮编
    private String postalCode;
    //收货地址国家码
    private String nationalCode;
    //省
    private String provinceName;
    //市
    private String cityName;
    //区
    private String countyName;
    //详细收货地址信息
    private String detailInfo;

    //默认
    private Integer is_default = 0;

    private String full_region;

    public String getFull_region() {
        if (StringUtils.isEmpty(full_region)) {
            full_region = "";
            if(null != getProvinceName() && getProvinceName().length() > 0){
                full_region += getProvinceName();
            }
            if(null != getCityName() && getCityName().length() > 0){
                full_region += getCityName();
            }
            if(null != getCountyName() && getCountyName().length() > 0){
                full_region += getCountyName();
            }
        }
        return full_region;
    }
}
