package com.acme.acmemall.service;

import java.util.Map;

public interface ITokenService {

    Map<String,Object> createToken(long userId);
}
