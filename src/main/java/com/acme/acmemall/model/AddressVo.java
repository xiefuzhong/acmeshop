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

    private Integer type = 0;

    /**
     * 地址拼接信息
     * @return
     */
    public String getFull_region() {
        //    return full_region;
        if (StringUtils.isNotEmpty(this.full_region)) {
            return full_region;
        } else {
            StringBuffer strBuff = new StringBuffer();

            if (StringUtils.isNotEmpty(this.provinceName)) {
                strBuff.append(this.provinceName).append(" ");
            }
            if (StringUtils.isNotEmpty(this.cityName)) {
                strBuff.append(this.cityName).append(" ");
            }
            if (StringUtils.isNotEmpty(this.countyName)) {
                strBuff.append(this.countyName).append(" ");
            }
//            if (StringUtils.isNotEmpty(this.detailInfo)) {
//                strBuff.append(this.detailInfo).append(" ");
//            }
            this.full_region = strBuff.toString();
            return this.full_region;
        }
    }

    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer("AddressVo{");
        sb.append("id=").append(id);
        sb.append(", userId=").append(userId);
//        sb.append(", userName='").append(userName).append('\'');
//        sb.append(", telNumber='").append(telNumber).append('\'');
        sb.append(", postalCode='").append(postalCode).append('\'');
        sb.append(", nationalCode='").append(nationalCode).append('\'');
        sb.append(", provinceName='").append(provinceName).append('\'');
        sb.append(", cityName='").append(cityName).append('\'');
        sb.append(", countyName='").append(countyName).append('\'');
        sb.append(", detailInfo='").append(detailInfo).append('\'');
        sb.append(", is_default=").append(is_default);
        sb.append(", full_region='").append(full_region).append('\'');
        sb.append('}');
        return sb.toString();
    }
}
