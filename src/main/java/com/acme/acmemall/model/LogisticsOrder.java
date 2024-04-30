package com.acme.acmemall.model;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @description:
 * @author: ihpangzi
 * @time: 2024/1/23 20:18
 */
@Data
public class LogisticsOrder implements Serializable {
    private String order_id;

    private String openid;

    private String delivery_id;

    private String biz_id;

    private String custom_remark;

    private long tagid;

    private int add_source;

    private String wx_appid;

    private Sender sender;

    private Receiver receiver;

    private Cargo cargo;

    private Shop shop;

    private Insured insured;

    private Service service;

    private String expect_time;

    private String take_mode;

    public void addOrder(OrderVo orderVo, List<OrderGoodsVo> orderGoodsList, LoginUserVo userVo) {
        setReceiverInfo(orderVo);
        setSenderInfo(orderVo.getShippingAddress());
        setShopInfo(orderVo, orderGoodsList);
        this.openid = userVo.getWeixin_openid();
        this.service = Service.builder().build();
        this.add_source = 0;

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
}
