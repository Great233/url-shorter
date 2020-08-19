package com.great.urlshorter.utils;

import java.util.Arrays;
import java.util.List;

/**
 * @author Great
 */
public class Base62 {

    private final static String[] DICT = {"0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "a", "b", "c", "d", "e", "f", "g", "h", "i", "j", "k", "l", "m", "n", "o", "p", "q", "r", "s", "t", "u", "v", "w", "x", "y", "z", "A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z"};

    public static String to62(long number) {
        StringBuilder result = new StringBuilder();
        while (number > 0) {
            int remainder = (int) (number % 62);
            result.insert(0, DICT[remainder]);
            number = (number - remainder) / 62;
        }
        return result.toString();
    }

    public static long to10(String base) {

        String[] list = base.split("");
        List<String> dict = Arrays.asList(Base62.DICT);

        long result = 0;
        for (String s : list) {
            result = 62 * result + dict.indexOf(s);
        }
        return result;
    }
}
