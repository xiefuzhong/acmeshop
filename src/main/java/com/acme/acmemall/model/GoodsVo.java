package com.acme.acmemall.model;

import com.acme.acmemall.common.GoodsHandleType;
import com.acme.acmemall.common.GoodsStatus;
import com.acme.acmemall.controller.reqeust.GoodsManageRequest;
import com.acme.acmemall.utils.DateUtils;
import com.acme.acmemall.utils.GsonUtil;
import com.acme.acmemall.utils.StringUtils;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.google.common.collect.Lists;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.apache.commons.collections.CollectionUtils;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.Timestamp;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

/**
 * spu
 */
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
public class GoodsVo implements Serializable {
    //主键
    private Long id;
    //商品类型Id
    private Integer category_id;
    //商品序列号
    private String goods_sn;
    //商品名称
    private String name;
    //品牌Id
    private Integer brand_id;
    //商品序列号
    @Builder.Default
    private Long goods_number = 0L;
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
    //删除状态(草稿)
    private Integer is_delete;
    //属性类别
    private Integer attribute_category;

    //专柜价格
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "##0.00")
    private BigDecimal counter_price;

    //附加价格
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "##0.00")
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
    @Builder.Default
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "##0.00")
    private BigDecimal market_price = BigDecimal.ZERO;

    //零售价格(现价)
    @Builder.Default
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "##0.00")
    private BigDecimal retail_price = BigDecimal.ZERO;
    //销售量
    @Builder.Default
    private Integer sell_volume = 0;
    //主sku　product_id
    private Integer primary_product_id;

    //单位价格，单价
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "##0.00")
    private BigDecimal unit_price;
    //推广描述
    private String promotion_desc;
    //推广标签
    private String promotion_tag;

    //APP专享价
    @Builder.Default
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "##0.00")
    private BigDecimal app_exclusive_price = BigDecimal.ZERO;
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

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "##0.00")
    private BigDecimal group_price;//团购价格(元)

    //产品小程序链接
    private String short_link;
    private LoginUserVo operator;

    private List<GoodsGalleryVo> galleryList = Lists.newArrayList();
    private List<GoodsSpecificationVo> specList = Lists.newArrayList();
    private List<ProductVo> products = Lists.newArrayList();

    public BigDecimal getCounter_price() {
        return counter_price == null ? BigDecimal.ZERO : counter_price.setScale(2, RoundingMode.HALF_UP);
    }

    public BigDecimal getExtra_price() {
        return extra_price == null ? BigDecimal.ZERO : extra_price.setScale(2, RoundingMode.HALF_UP);
    }

    public BigDecimal getMarket_price() {
        return market_price == null ? BigDecimal.ZERO : market_price.setScale(2, RoundingMode.HALF_UP);
    }

    public BigDecimal getRetail_price() {
        return retail_price == null ? BigDecimal.ZERO : retail_price.setScale(2, RoundingMode.HALF_UP);
    }

    public BigDecimal getUnit_price() {
        return unit_price == null ? BigDecimal.ZERO : unit_price.setScale(2, RoundingMode.HALF_UP);
    }

    public BigDecimal getApp_exclusive_price() {
        return app_exclusive_price == null ? BigDecimal.ZERO : app_exclusive_price.setScale(2, RoundingMode.HALF_UP);
    }

    public BigDecimal getGroup_price() {
        return group_price == null ? BigDecimal.ZERO : group_price.setScale(2, RoundingMode.HALF_UP);
    }

    /**
     * 更新商品
     *
     * @param handle  操作 上/下架、删除(草稿中)
     * @param request 待修改数据
     */
    public void update(GoodsHandleType handle, GoodsManageRequest request) {
        switch (handle) {
            case OFF: {
                this.notOnSale();
                break;
            }
            case ON: {
                this.onSale();
                break;
            }
            case DRAFT: {
                this.draft();
                break;
            }
            case DELETE: {
                this.delete();
                break;
            }

        }
    }

    /**
     * 商品上架
     */
    private void onSale() {
        this.is_on_sale = GoodsStatus.ON_SALE.getStatusCode();
    }

    /**
     * 商品下架
     */
    private void notOnSale() {
        this.is_on_sale = GoodsStatus.NOT_ON_SALE.getStatusCode();
    }

    /**
     * 删除/进入草稿中
     */
    private void draft() {
        this.is_on_sale = GoodsStatus.IN_DRAFT.getStatusCode();
    }

    private void delete() {
        this.is_delete = 1;
    }

    /**
     * 是否下架
     *
     * @return
     */
    public boolean checkOff() {
        return this.is_delete.equals(1) || !this.is_on_sale.equals(1);
    }

    public String getFmt_add_time() {
        if (this.add_time == null) {
            return null;
        }
        return DateUtils.timeToUtcDate(this.add_time.getTime(), DateUtils.DATE_TIME_PATTERN);
    }

    public boolean check() {
        if (StringUtils.isNullOrEmpty(name)) {
            return Boolean.FALSE;
        }
        return Boolean.TRUE;
    }

    public void updateStock(ProductVo product) {
        this.goods_number = product.getGoods_number();
        this.goods_sn = product.getGoods_sn();
        this.retail_price = product.getRetail_price();
    }

    /**
     * 计算库存：各SKU累加
     *
     * @param products
     */
    public void calSku(List<ProductVo> products) {
        if (CollectionUtils.isEmpty(products)) {
            return;
        }
        // 使用流来计算商品总数，增加了空值检查
        Long sum_goods_number = products.stream()
                .filter(product -> product.getGoods_number() != null)
                .mapToLong(product -> product.getGoods_number())
                .sum();
        if (sum_goods_number > 0) {
            this.goods_number = sum_goods_number;
        }
        this.goods_sn = products.stream().findFirst().get().getGoods_sn();

        // 优化最小零售价的计算，增加了对null的检查并避免了潜在的最小值错误
        BigDecimal min_retail_price = products.stream()
                .filter(product -> product.getRetail_price() != null)
                .map(product -> product.getRetail_price())
                .min(Comparator.naturalOrder())
                .orElse(BigDecimal.ZERO);
        // 仅当最小价格大于0时，更新零售价
        if (min_retail_price.compareTo(BigDecimal.ZERO) > 0) {
            this.retail_price = min_retail_price;
        }
    }

    public void relatedDetails(String type, List<?> items) {
        switch (type) {
            case "gallery": {
                items.stream().map(object -> (GoodsGalleryVo) object).forEach(galleryVo -> this.galleryList.add(galleryVo));
                break;
            }
            case "spec": {
                items.stream().map(object -> (GoodsSpecificationVo) object).forEachOrdered(specificationVo -> this.specList.add(specificationVo));
                break;
            }
            case "product": {
                items.stream().map(object -> (ProductVo) object).forEach(productVo -> this.products.add(productVo));
                break;
            }
        }
    }

    @Override
    public String toString() {
        return GsonUtil.getGson().toJson(this);
    }
}
