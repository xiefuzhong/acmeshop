package com.acme.acmemall.kuaidi100.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * @description:
 * @author: ihpangzi
 * @time: 2024/1/24 10:11
 */
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
public class QueryTrackRequest extends BaseRequest {
    /**
     * 我方分配给贵司的的公司编号, 点击查看账号信息
     */
    private String customer;
    /**
     * 签名， 用于验证身份， 按param + key + customer 的顺序进行MD5加密（注意加密后字符串要转大写）， 不需要“+”号
     */
    private String sign;
    /**
     * 其他参数组合成的json对象
     */
    private String param;
}
