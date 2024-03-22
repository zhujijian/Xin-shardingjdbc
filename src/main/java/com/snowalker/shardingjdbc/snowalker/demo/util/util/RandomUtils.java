package com.snowalker.shardingjdbc.snowalker.demo.util.util;

import java.util.concurrent.ThreadLocalRandom;

/***
 * 随机数工具类
 */
public class RandomUtils {

    private static final String ALPHA_NUMBER = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789abcdefghijklmnopqrstuvwxyz";
    private static final String NUMBER = "0123456789";

    // 私有构造方法
    private RandomUtils() {
    }

    /** 生成纯数字随机数 */
    public static String randomNum(int len) {
        return random(NUMBER, len);
    }

    /** 生成字符串随机数 */
    public static String randomStr(int len) {
        return random(ALPHA_NUMBER, len);
    }

    private static String random(String base, int len) {
        StringBuilder sb = new StringBuilder();
        ThreadLocalRandom current = ThreadLocalRandom.current();
        for (int i = 0; i < len; i++) {
            int number = current.nextInt(base.length());
            sb.append(base.charAt(number));
        }
        return sb.toString();
    }

}
