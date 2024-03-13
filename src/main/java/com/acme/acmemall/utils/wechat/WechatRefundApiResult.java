package com.acme.acmemall.utils.wechat;

import lombok.Data;

@Data
public class WechatRefundApiResult {
    private String return_code;
    private String return_msg;

    /**
     * SUCCESS/FAIL<br>
     * <p>
     * SUCCESS退款申请接收成功，结果通过退款查询接口查询<br>
     * FAIL 提交业务失败
     */
    private String result_code;
    private String err_code;
    private String err_code_des;
    /**
     * 服务商商户的APPID
     */
    private String appid;
    /**
     * 微信支付分配的商户号
     */
    private String mch_id;
    /**
     * 微信分配的小程序ID
     */
    private String sub_appid;
    /**
     * 微信支付分配的子商户号
     */
    private String sub_mch_id;
    private String device_info;
    private String nonce_str;
    private String sign;
    /**
     * 微信订单号
     */
    private String transaction_id;
    /**
     * 商户订单号:商户系统内部订单号，要求32个字符内，只能是数字、大小写字母_-
     */
    private String out_trade_no;
    /**
     * 商户退款单号，商户系统内部的退款单号，商户系统内部唯一，同一退款单号多次请求只退一笔
     */
    private String out_refund_no;
    /**
     * 微信退款单号
     */
    private String refund_id;
    /**
     * 退款渠道: <br>
     * ORIGINAL—原路退款<br>
     * BALANCE—退回到余额<br>
     * OTHER_BALANCE—原账户异常退到其他余额账户<br>
     * OTHER_BANKCARD—原银行卡异常退到其他银行卡<br>
     */
    private String refund_channel;
    /**
     * 申请退款金额:退款总金额,单位为分,可以做部分退款
     */
    private String refund_fee;
    /**
     * 退款金额:退款总金额,单位为分,可以做部分退款
     */
    private String settlement_refund_fee;
    /**
     * 总金额:订单总金额，单位为分，只能为整数
     */

    private String total_fee;
    /**
     * 结算金额:退款金额+代金券退款金额，退款金额<=订单金额，退款金额+代金券退款金额=订单金额
     */

    private String settlement_total_fee;
    /**
     * 货币类型:默认人民币：CNY
     */

    private String fee_type;
    /**
     * 现金支付金额，单位为分，
     */
    private String cash_fee;
    /**
     * 现金退款金额，单位为分，
     */
    private String cash_refund_fee;
    /**
     * 退款状态
     */
    private String refund_status;

    /**
     * 退款成功时间，当退款状态为退款成功时有返回
     */
    private String refund_success_time;

}
