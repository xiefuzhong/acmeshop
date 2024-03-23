package com.acme.acmemall.controller.reqeust;

import com.acme.acmemall.model.enums.CouponTypeEnum;
import com.acme.acmemall.model.enums.ScopeEnum;
import com.acme.acmemall.utils.StringUtils;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * @description:
 * @author: ihpangzi
 * @time: 2024/3/23 11:40
 */
@Data
public class CouponRequest implements Serializable {
    private Integer type; // 0:满减 1:折扣

    // 适用类型 通用券-0，商品券-1，品类券-2  商品券-需要指定适用商品，品类券-需要指定适用品类
    private Integer scope;

    //优惠券名称
    private String name;

    //金额/面值  优惠券类型-0-满减券-金额数值，优惠券类型-1-折扣券-百分比
    private BigDecimal type_money;

    // 领取方式 手动领取(8用户主动领取优惠券)、后台发放  手动领取：用户需要在移动端的领券中心领取优惠券；后台发放：后台发放用于后台发放指定用户使用，移动端不能领取；
    private Integer send_type;

    //    使用门槛 最低消费金额为0,即无门槛。大于0.0元，即达到这个金额可用。
    //最小金额
    private BigDecimal min_amount;

    // 使用时间(天-根据领取和发放计算领取和使用有效期)
    private Integer usageTime;

    //1:可用 0：不可用
    private Integer enabled;

    // 限量(领取/发放)
    private Integer limit;

    // 发行量
    private Integer total;

    private Long userId;


    /**
     * 校验请求参数
     *
     * @return
     */
    public boolean checkRequest() {
        Boolean result = CouponTypeEnum.verify(this.type)
                && ScopeEnum.verify(this.scope)
                && StringUtils.isNotEmpty(this.name);
        return result;
    }

}
