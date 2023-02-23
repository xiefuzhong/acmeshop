package com.acme.acmemall.model;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 购物车模型
 *
 * @author ihpangzi
 * @date 2017-08-15 08:03:39
 */
@Data
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
    //
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
     * @return
     */
    public BigDecimal getGoodsTotalAmount() {
        if (retail_price != null && number != null) {
            return retail_price.multiply(new BigDecimal(number));
        }
        return BigDecimal.ZERO;
    }

    // 计算运费
    public BigDecimal getExtraPrice(){
        if (extra_price != null && number != null) {
            return extra_price.multiply(new BigDecimal(number));
        }
        return BigDecimal.ZERO;
    }

}
