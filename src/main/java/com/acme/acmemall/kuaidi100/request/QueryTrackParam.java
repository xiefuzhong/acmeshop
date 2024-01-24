package com.acme.acmemall.kuaidi100.request;

import com.acme.acmemall.exception.Assert;
import com.acme.acmemall.utils.GsonUtil;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
public class QueryTrackParam {
    /**
     * 查询的快递公司的编码，一律用小写字母
     */
    private String com;
    /**
     * 查询的快递单号， 单号的最大长度是32个字符
     */
    private String num;
    /**
     * 收件人或寄件人的手机号或固话
     */
    private String phone;
    /**
     * 出发地城市，省-市-区
     */
    private String from;
    /**
     * 目的地城市，省-市-区
     */
    private String to;
    /**
     * 添加此字段表示开通行政区域解析功能。0：关闭（默认），1：开通行政区域解析功能，2：开通行政解析功能并且返回出发、目的及当前城市信息
     */
    @Builder.Default
    private String resultv2 = "0";
    /**
     * 返回数据格式。0：json（默认），1：xml，2：html，3：text
     */
    @Builder.Default
    private String show = "0";
    /**
     * 返回结果排序方式。desc：降序（默认），asc：升序
     */
    @Builder.Default
    private String order = "desc";

    private void check() {
        Assert.isBlank(this.com, "快递公司的编码不能为空!");
        Assert.isBlank(this.num, "快递单号不能为空!");
    }

    public String toParam() {
        this.check();
        return GsonUtil.toJson(this);
    }
}
