package com.acme.acmemall.model;

import com.acme.acmemall.utils.StringUtils;
import lombok.Builder;
import lombok.Getter;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.UUID;

/**
 * 购物车模型
 *
 * @author ihpangzi
 * @date 2017-08-15 08:03:39
 */
@Getter
@Builder
public class ShopCartVo implements Serializable {
    private static final long serialVersionUID = 1L;

    //主键
    private Integer id;
    //会员Id
    private Long user_id;
    //sessionId
    private String session_id;
    //商品Id
    private Integer goods_id;
    //商品序列号
    private String goods_sn;
    //产品Id
    private Integer product_id;
    //产品名称
    private String goods_name;
    //市场价
    private BigDecimal market_price;
    //零售价格
    private BigDecimal retail_price;
    //product表中的零售价格
    private BigDecimal retail_product_price;
    //数量
    private Integer number;
    //规格属性组成的字符串，用来显示用
    private String goods_specifition_name_value;
    //product表对应的goods_specifition_ids
    private String goods_specifition_ids;
    // 1-选中
    private Integer checked;
    // 节省金额
    private BigDecimal crash_save_price;

    //商品图片
    private String list_pic_url;

    //商户ID
    private Long merchant_id;

    // 运费
    private BigDecimal extra_price;

    /**
     * 节省金额计算=产品零售价格-实际零售价格
     *
     * @return 节省金额
     */
    public BigDecimal getCrash_save_price() {
        if (null == crash_save_price && null != retail_product_price && null != retail_price) {
            crash_save_price = retail_product_price.subtract(retail_price);
        }
        return crash_save_price;
    }

    /**
     * 商品总额=商品价格*数量
     *
     * @return
     */
    public BigDecimal getGoodsTotalAmount() {
        if (retail_price != null && number != null) {
            return retail_price.multiply(new BigDecimal(number));
        }
        return BigDecimal.ZERO;
    }

    // 计算运费
    public BigDecimal getExtraPrice() {
        if (extra_price != null && number != null) {
            return extra_price.multiply(new BigDecimal(number));
        }
        return BigDecimal.ZERO;
    }

    // 添加到购物车
    public void addToCart(GoodsVo goods, ProductVo product, Integer number) {
        if (goods != null) {
            this.merchant_id = goods.getMerchantId();
            this.goods_id = goods.getId();
            this.goods_name = goods.getName();
            this.goods_sn = goods.getGoods_sn();
            this.list_pic_url = goods.getList_pic_url();
        }
        if (product != null) {
            this.product_id = product.getProduct_id();
            this.retail_price = product.getRetail_price();
            this.market_price = product.getMarket_price();
            this.goods_specifition_ids = product.getGoods_specification_ids();
            if (!StringUtils.isNullOrEmpty(product.getGoods_specifition_name_value())) {
                this.goods_specifition_name_value = product.getGoods_specifition_name_value();
            }
        }
        this.checked = 1;
        this.session_id = UUID.randomUUID().toString().replaceAll("-", "");
        this.number = number;
    }

    public void updateRetailPrice() {
        this.retail_price = this.retail_product_price;
    }

}
