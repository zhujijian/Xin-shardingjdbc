package com.snowalker.shardingjdbc.snowalker.demo.util.util;

import java.util.Random;

public class InviteCodeUtil {

    public static String generateInviteCode(int len) {

        String str = "";
        char[] chars = {'Q', 'W','q', 'E','w', '8','e','S','d', '2', 's','D', 'Z',
                'X','z', '9', 'x','C', '7','c', 'P', '5', 'p','K', '3',
                'k','M', 'J','M', 'U', 'F','j', 'R', '4','u', 'V','f' ,'Y','r',
                'v','T','y', 'N','t', '6','n' ,'B', 'G','b' ,'H','g' ,'A','L', 'a'};
        Random random = new Random();
        char[] inviteChars = new char[len];
        for (int i = 0; i < len; i++) {
            inviteChars[i] = chars[random.nextInt(chars.length)];
        }
        str = String.valueOf(inviteChars);
        return str;
    }

}
