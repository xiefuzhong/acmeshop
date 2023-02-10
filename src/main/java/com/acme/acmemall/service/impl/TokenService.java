package com.acme.acmemall.service.impl;

import com.acme.acmemall.dao.TokenMapper;
import com.acme.acmemall.model.Token;
import com.acme.acmemall.service.ITokenService;
import com.acme.acmemall.utils.CharUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Service
public class TokenService implements ITokenService {

    //12小时后过期
    private final static int EXPIRE = 3600 * 12;
    //2小时后过期
    private final static int EXPIRE2 = 3600 * 2;
    private final TokenMapper tokenDao;

    @Autowired
    public TokenService(TokenMapper tokenDao) {
        this.tokenDao = tokenDao;
    }


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

        Map<String, Object> map = new HashMap<>();
        map.put("token", token);
        map.put("expire", EXPIRE2);
        return map;
    }
}
