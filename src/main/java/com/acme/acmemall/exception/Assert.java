package com.acme.acmemall.exception;

import org.apache.commons.lang.StringUtils;

/**
 * 数据校验
 *
 * @author admin
 * @date 2017-03-23 15:50
 */
public abstract class Assert {

    public static void isBlank(String str, String message) {
        if (StringUtils.isBlank(str)) {
            throw new ApiCusException(message);
        }
    }

    public static void isNull(Object object, String message) {
        if (object == null) {
            throw new ApiCusException(message);
        }
    }
}
