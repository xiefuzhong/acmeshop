package com.acme.acmemall.model;

import com.acme.acmemall.utils.DateUtils;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * @description:
 * @author: ihpangzi
 * @time: 2023/2/14 9:02
 */
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class FootprintVo implements Serializable {
    //主键
    private Integer id;
    //会员Id
    private Long user_id;
    //商品id
    private Integer goods_id;
    //记录时间
    private Long add_time;
    //推荐人
    @Builder.Default
    private Long referrer = 0L;

    // 商品冗余字段
    private String name;
    private String list_pic_url;
    private String goods_brief;
    //
    private BigDecimal retail_price;
    // 会员
    private String nickname;
    private String avatar;

    private String fmt_add_time;

    private String sort_add_time;

    public void record(GoodsVo goodsVo, Long referrer) {
        if (null != referrer) {
            this.referrer = referrer;
        }
        this.add_time = System.currentTimeMillis() / 1000;
        this.goods_brief = goodsVo.getGoods_brief();
        this.list_pic_url = goodsVo.getList_pic_url();
        this.goods_id = goodsVo.getId();
        this.name = goodsVo.getName();
        this.retail_price = goodsVo.getRetail_price();
    }

    public boolean operationCheck(LoginUserVo user) {
        if (this.goods_id == null) {
            return false;
        }
        return user != null && this.user_id == user.getUserId();
    }

    /**
     * 时间格式化
     */
    public void fmtAddTime() {
        this.fmt_add_time = DateUtils.timeToUtcDate(this.add_time, DateUtils.DATE_TIME_PATTERN);
        this.sort_add_time = DateUtils.timeToStr(this.add_time, DateUtils.DATE_PATTERN);
    }
}
