package com.acme.acmemall.model;

import com.acme.acmemall.utils.DateUtils;
import com.acme.acmemall.utils.GsonUtil;
import com.acme.acmemall.utils.StringUtils;
import lombok.*;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Date;

/**
 * spu
 */
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
public class GoodsVo implements Serializable {
    //主键
    private Integer id;
    //商品类型Id
    private Integer category_id;
    //商品序列号
    private String goods_sn;
    //商品名称
    private String name;
    //品牌Id
    private Integer brand_id;
    //商品序列号
    private Integer goods_number;
    //关键字
    private String keywords;
    //简明介绍
    private String goods_brief;
    //商品描述
    private String goods_desc;
    //上架
    private Integer is_on_sale;

    //添加时间
    private Date add_time;

    private String fmt_add_time;
    //排序
    private Integer sort_order;
    //删除状态
    private Integer is_delete;
    //属性类别
    private Integer attribute_category;
    //专柜价格
    private BigDecimal counter_price;
    //附加价格
    private BigDecimal extra_price;
    //是否新商品
    private Integer is_new;
    //商品单位
    private String goods_unit;
    //商品主图
    private String primary_pic_url;
    //商品列表图
    private String list_pic_url;
    //市场价
    private BigDecimal market_price;
    //零售价格(现价)
    private BigDecimal retail_price;
    //销售量
    private Integer sell_volume;
    //主sku　product_id
    private Integer primary_product_id;
    //单位价格，单价
    private BigDecimal unit_price;
    //推广描述
    private String promotion_desc;
    //推广标签
    private String promotion_tag;
    //APP专享价
    private BigDecimal app_exclusive_price;
    //是否是APP专属
    private Integer is_app_exclusive;
    //限购
    private Integer is_limited;
    //热销
    private Integer is_hot;
    //购物车中商品数量
    private Integer cart_num = 0;
    // 冗余
    // 产品Id
    private Integer product_id;
    //是否秒杀商品
    private Integer is_secKill;
    //秒杀开始时间
    private Timestamp start_time;
    //是否是服务型商品
    private Integer is_service;
    //新增
    //分佣百分比
    private double brokerage_percent;
    //品牌名称
    private String brand_name;
    //商户id
    private Long merchantId;
    //折扣
    private String discount;
    //分佣比例
    private String user_brokerage_price;
    private Timestamp end_time;//活动结束时间
    private Integer success_time;//成团时间 单位分钟
    private Integer success_people;//成团人数
    private BigDecimal group_price;//团购价格(元)

    //产品小程序链接
    private String short_link;

    /**
     * 是否下架
     *
     * @return
     */
    public boolean checkOff() {
        return this.is_delete.equals(1) || !this.is_on_sale.equals(1);
    }

    public String getFmt_add_time() {
        if (this.add_time == null)
            return null;
        return DateUtils.timeToUtcDate(this.add_time.getTime(), DateUtils.DATE_TIME_PATTERN);
    }

    public boolean check() {
        if (StringUtils.isNullOrEmpty(name)) {
            return Boolean.FALSE;
        }
        return Boolean.TRUE;
    }

    @Override
    public String toString() {
        return GsonUtil.getGson().toJson(this);
    }
}
