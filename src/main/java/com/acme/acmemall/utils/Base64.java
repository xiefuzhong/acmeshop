package com.acme.acmemall.utils;

import java.io.UnsupportedEncodingException;

/**
 * 作者: @author admin <br>
 * 时间: 2017-08-11 16:17<br>
 * 描述: Base64 <br>
 */
public class Base64 {
	public static void main(String[] args) {
		System.out.println(decode("6buE5YGl5a6J"));
	}


    // 加密
    public static String encode(String str) {
        byte[] b = null;
        String s = null;
        try {
            b = str.getBytes("utf-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        if (b != null) {
            s = java.util.Base64.getEncoder().encodeToString(b);
        }
        return s;
    }

    // 解密
    public static String decode(String s) {
        byte[] b = null;
        String result = null;
        if (s != null) {
            try {
                b = java.util.Base64.getDecoder().decode(s);
                result = new String(b, "utf-8");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return result;
    }
}
