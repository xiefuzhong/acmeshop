package com.acme.acmemall.utils;

/**
 * 描述: UserUtils <br>
 */
public class UserUtils {

    //替换字符串
    public static String getCode(String APPID, String REDIRECT_URI, String SCOPE) {
        return String.format(ResourceUtil.getConfigByName("wx.getCode"), APPID, REDIRECT_URI, SCOPE);
    }

    // 小程序登录接口
    public static String getWebAccess(String CODE) {
        return String.format(ResourceUtil.getConfigByName("wx.webAccessTokenhttps"),
                ResourceUtil.getConfigByName("wx.appId"),
                ResourceUtil.getConfigByName("wx.secret"),
                CODE);
    }

    //替换字符串
    public static String getUserMessage(String access_token, String openid) {
        return String.format(ResourceUtil.getConfigByName("wx.userMessage"), access_token, openid);
    }


    //替换字符串
    public static String getAccessToken() {
        return String.format(ResourceUtil.getConfigByName("wx.getAccessToken"),
                ResourceUtil.getConfigByName("wx.appId"),
                ResourceUtil.getConfigByName("wx.secret"));
    }

    public static String getWXACodeUnlimit(String accessToken, String scene) {
        return String.format(ResourceUtil.getConfigByName("wx.getWXACodeUnlimit"), accessToken, scene);
    }

    /**
     * 微信物流助手-生成运单
     *
     * @param accessToken
     * @return
     */
    public static String getWxAddLogisticsOrder(String accessToken) {
        return String.format(ResourceUtil.getConfigByName("wx.addLogisticsOrder"), accessToken);
    }

    public static String getUserPhoneNumber(String accessToken) {
        return String.format(ResourceUtil.getConfigByName("wx.getuserphonenumber"), accessToken);
    }

    /**
     * 获取电子面单余额-获取电子面单余额
     *
     * @param accessToken
     * @return
     */
    public static String getWxLogistisQuota(String accessToken) {
        return String.format(ResourceUtil.getConfigByName("wx.getWxExpressQuota"), accessToken);
    }

    /**
     * 获取电子面单余额-获取运单数据
     *
     * @param accessToken
     * @return
     */
    public static String getWxExpressOrder(String accessToken) {
        return String.format(ResourceUtil.getConfigByName("wx.getWxExpressOrder"), accessToken);
    }

    /**
     * 获取电子面单余额-查询运单轨迹
     *
     * @param accessToken
     * @return
     */
    public static String getWxExpressTrack(String accessToken) {
        return String.format(ResourceUtil.getConfigByName("wx.getWxExpressTrack"), accessToken);
    }

    /**
     * 获取电子面单余额-模拟更新订单状态
     *
     * @param accessToken
     * @return
     */
    public static String testUpdateExpressOrder(String accessToken) {
        return String.format(ResourceUtil.getConfigByName("wx.testUpdateExpressOrder"), accessToken);
    }
}
