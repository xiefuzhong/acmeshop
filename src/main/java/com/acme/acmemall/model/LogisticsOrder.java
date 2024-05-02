package com.acme.acmemall.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

/**
 * @description:
 * @author: ihpangzi
 * @time: 2024/1/23 20:18
 */
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder
public class LogisticsOrder implements Serializable {
    private String order_id; // 必填

    private String openid; // 用户openid add_source =0必填,订单来源，0为小程序订单，2为App或H5订单，填2则不发送物流服务通知

    private String delivery_id; // 快递公司ID，参见getAllDelivery

    private String biz_id; // 快递客户编码或者现付编码

    private String custom_remark; // 快递备注信息，比如"易碎物品"，不超过1024字节

    private long tagid; // 订单标签id，用于平台型小程序区分平台上的入驻方，tagid须与入驻方账号一一对应，非平台型小程序无需填写该字段

    private int add_source = 0; // 订单来源，0为小程序订单，2为App或H5订单，填2则不发送物流服务通知

    private String wx_appid;

    private Sender sender; // 发件人信息

    private Receiver receiver; // 收件人信息

    private Cargo cargo; // 收件人信息

    private Shop shop; // 收件人信息

    private Insured insured; // 	保价信息

    private Service service; // 服务类型

    private long expect_time; // 顺丰必须传。 预期的上门揽件时间，0表示已事先约定取件时间；否则请传预期揽件时间戳，需大于当前时间，收件员会在预期时间附近上门。

    private long take_mode; // 分单策略，【0：线下网点签约，1：总部签约结算】，不传默认线下网点签约。目前支持圆通。

    public void addOrder(OrderVo orderVo, List<OrderGoodsVo> orderGoodsList) {
        setReceiverInfo(orderVo);
        setSenderInfo(orderVo.getShippingAddress());
        setShopInfo(orderVo, orderGoodsList);
        setCargoInfo();
        this.service = Service.builder().build();
    }

    private void setShopInfo(OrderVo orderVo, List<OrderGoodsVo> orderGoodsList) {
        this.shop = Shop.builder()
                .wxa_path("")
                .goods_count(orderVo.getGoodsCount())
                .goods_name(orderVo.getGoods_name())
                .img_url(orderGoodsList.get(0).getList_pic_url())
                .build();
    }

    private void setSenderInfo(AddressVo addressVo) {
        this.sender = Sender.builder()
                .name(addressVo.getUserName())
                .province(addressVo.getProvinceName())
                .city(addressVo.getCityName())
                .area(addressVo.getCountyName())
                .address(addressVo.getDetailInfo())
                .mobile(addressVo.getTelNumber())
                .build();
    }

    private void setReceiverInfo(OrderVo orderVo) {
        this.receiver = Receiver.builder()
                .name(orderVo.getConsignee())
                .province(orderVo.getProvince())
                .city(orderVo.getCity())
                .area(orderVo.getDistrict())
                .address(orderVo.getAddress())
                .mobile(orderVo.getMobile())
                .build();
    }

    public void setCargoInfo() {
        this.cargo = Cargo.builder()
                .count(1)
                .weight(BigDecimal.ONE)
                .space_x(BigDecimal.TEN)
                .space_y(BigDecimal.TEN)
                .space_z(BigDecimal.TEN)
                .build();
    }
}
