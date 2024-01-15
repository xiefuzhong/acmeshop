package com.acme.acmemall.common;

import com.acme.acmemall.exception.ResultCodeEnum;

import java.util.HashMap;
import java.util.Map;

/**
 * 返回数据
 *
 * @author IHPANGZI
 * @date 2023-02-07
 */
public class ResultMap extends HashMap<String, Object> {
    private static final long serialVersionUID = 1L;

    public ResultMap() {
        put("errno", 0);
        put("errmsg", "操作成功");
    }

    public static ResultMap error() {
        return error(500, "未知异常，请联系管理员");
    }

    public static ResultMap error(String msg) {
        return error(500, msg);
    }

    public static ResultMap response(ResultCodeEnum resultCode) {
        ResultMap resultMap = new ResultMap();
        resultMap.put("errno", resultCode.getCode());
        resultMap.put("errmsg", resultCode.getMessage());
        return resultMap;
    }

    public static ResultMap response(ResultCodeEnum resultCode, Object data) {
        ResultMap resultMap = response(resultCode);
        resultMap.put("data", data);
        return resultMap;
    }

    public static ResultMap error(int code, String msg) {
        ResultMap r = new ResultMap();
        r.put("errno", code);
        r.put("errmsg", msg);
        return r;
    }

    public static ResultMap ok(String msg) {
        ResultMap r = new ResultMap();
        r.put("errmsg", msg);
        return r;
    }

    public static ResultMap ok(Map<String, Object> map) {
        ResultMap r = new ResultMap();
        r.putAll(map);
        return r;
    }

    public static ResultMap ok() {
        return new ResultMap();
    }

    public static ResultMap badArgument() {
        return error(1001, "参数不对");
    }

    public ResultMap put(String key, Object value) {
        super.put(key, value);
        return this;
    }
}
