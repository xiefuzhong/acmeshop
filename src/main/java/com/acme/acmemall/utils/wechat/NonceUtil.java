package com.acme.acmemall.utils.wechat;

import java.security.SecureRandom;
import java.util.Locale;

public class NonceUtil {

  private NonceUtil() {}

  private static final char[] SYMBOLS =
      "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ".toCharArray();
  private static final SecureRandom random = new SecureRandom();

  /**
   * 使用SecureRandom生成随机串
   *
   * @param length 随机串长度 18-32
   * @return nonce 随机串
   */
  public static String createNonce(int length) {
    char[] buf = new char[length];
    for (int i = 0; i < length; ++i) {
      buf[i] = SYMBOLS[random.nextInt(SYMBOLS.length)];
    }
    return new String(buf).toUpperCase(Locale.ROOT);
  }
}