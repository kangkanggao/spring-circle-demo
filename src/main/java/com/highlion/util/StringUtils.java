package com.highlion.util;

public class StringUtils {

    public static String lowerFirst(String oldStr) {
        char[] chars = oldStr.toCharArray();
        chars[0] += 32;
        return String.valueOf(chars);
    }
}
