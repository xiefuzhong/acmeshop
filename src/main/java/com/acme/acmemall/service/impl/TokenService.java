package com.acme.acmemall.service.impl;

import com.acme.acmemall.dao.TokenMapper;
import com.acme.acmemall.model.Token;
import com.acme.acmemall.service.ITokenService;
import com.acme.acmemall.utils.CharUtil;
import com.acme.acmemall.utils.StringUtils;
import com.acme.acmemall.utils.UserUtils;
import com.acme.acmemall.utils.cache.J2EcacheUtil;
import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Maps;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Date;
import java.util.Map;

@Service
public class TokenService implements ITokenService {

    //12小时后过期
    private final static int EXPIRE = 3600 * 12;
    //2小时后过期
    private final static int EXPIRE2 = 3600 * 2;
    private final TokenMapper tokenDao;

    protected Logger logger = Logger.getLogger(getClass());

    @Autowired
    public TokenService(TokenMapper tokenDao) {
        this.tokenDao = tokenDao;
    }

    @Autowired
    private RestTemplate restTemplate;


    /**
     * @param userId
     * @return
     */
    @Override
    public Map<String, Object> createToken(long userId) {
        //生成一个token
        String tokenStr = CharUtil.getRandomString(32);
        //当前时间
        Date now = new Date();

        //过期时间
        Date expireTime = new Date(now.getTime() + EXPIRE2 * 1000);

        //判断是否生成过token
        Token token = tokenDao.queryByUserId(userId);
        if (token == null) {
            token = new Token();
            token.setUserId(userId);
            token.setToken(tokenStr);
            token.setUpdateTime(now);
            token.setExpireTime(expireTime);

            //保存token
            tokenDao.save(token);
        } else {
            token.setToken(tokenStr);
            token.setUpdateTime(now);
            token.setExpireTime(expireTime);

            //更新token
            tokenDao.update(token);
        }

        Map<String, Object> map = Maps.newHashMap();
        map.put("token", token);
        map.put("expire", EXPIRE2);
        return map;
    }

    /**
     * @param merchantId
     * @return
     */
    @Override
    public Map<String, Object> getTokens(long merchantId) {
        String tokenStr = (String) J2EcacheUtil.getInstance().get(J2EcacheUtil.SHOP_CACHE_NAME, String.valueOf(merchantId));
        logger.info("cache:" + tokenStr.length());
        Map<String, Object> map = Maps.newHashMap();
        if (StringUtils.isNotEmpty(tokenStr)) {
            map.put("token", tokenStr);
            return map;
        } else {
            Token token = tokenDao.queryByUserId(merchantId);
            if (token == null) {
                String requestUrl = UserUtils.getAccessToken();
                logger.info("》》》requestUrl为：" + requestUrl);
                String res = restTemplate.getForObject(requestUrl, String.class);
                logger.info("res==" + res);
                JSONObject tokenData = JSONObject.parseObject(res);
                map.put("token", tokenData.getString("access_token"));
                Date now = new Date();
                //过期时间
                Date expireTime = new Date(now.getTime() + tokenData.getLong("expires_in"));
                token = new Token();
                token.setUserId(merchantId);
                token.setToken(tokenData.getString("access_token"));
                token.setUpdateTime(now);
                token.setExpireTime(expireTime);
                //保存token
                tokenDao.save(token);
            }
        }
        return map;
    }
}
