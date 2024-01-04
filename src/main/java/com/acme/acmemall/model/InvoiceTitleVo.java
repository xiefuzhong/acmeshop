package com.acme.acmemall.model;

import lombok.Data;

import java.io.Serializable;

/**
 * @description:
 * @author: ihpangzi
 * @time: 2024/1/4 16:50
 */
//@Data
@Data
public class InvoiceTitleVo implements Serializable {
    //主键
    private Long id;
    //会员ID
    private Long userId;
    // 抬头类型 0-单位,1-个人
    private Integer type;
    //  抬头名称
    private String title;

    // 税号
    private String taxNumber;

    private String companyAddress; // 企业地址

    private String telephone; // 手机号

    private String bankName; // 银行名称

    private String bankAccount; // 银行账号

    private Integer invoiceType; // 发票类型，企业有增值税电子专用发票，个人只有电子普通发票，1-电子专用，0-普票

    private String email; // 邮箱

    private String companyTel; // 企业电话

    //默认
    private Integer is_default = 0;

}
