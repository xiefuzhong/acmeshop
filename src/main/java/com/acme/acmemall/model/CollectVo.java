package com.acme.acmemall.model;

import com.acme.acmemall.exception.ApiCusException;
import lombok.Builder;
import lombok.Getter;

import java.io.Serializable;

/**
 * @description:用户收藏的商品信息
 * @author: ihpangzi
 * @time: 2023/2/14 10:17
 */
@Getter
@Builder
public class CollectVo implements Serializable {
    //主键
    private Integer id;
    //用户Id
    private Long user_id;
    //产品Id
    private Integer value_id;
    //添加时间
    private Long add_time;
    //是否是关注
    private Integer is_attention;
    //
    private Integer type_id;
    //
    private String name;
    private String list_pic_url;
    private String goods_brief;
    private String retail_price;

    /**
     * 添加收藏
     * @param userId
     * @param value_id
     * @param type_id
     */
    public void addCollect(Long userId, Integer value_id, Integer type_id) {
        this.user_id = userId;
        this.value_id = value_id;
        this.type_id = type_id;
        this.is_attention = 0;
    }

    public void deleteCollect(Long userId, Integer value_id) {
        if (!userId.equals(this.user_id) || !value_id.equals(value_id)){
            throw new ApiCusException("越权操作!");
        }
        this.is_attention = 1;
    }
}
