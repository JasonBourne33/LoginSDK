package com.example.util;

import java.util.Random;

// generate random String
public class StringUtil {

    public static final String SOURCES =
            "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz1234567890";
    public static final String NUM_SOURCES =
            "1234567890";

    public static String generateString(Random random, int length) {
        char[] text = new char[length];
        for (int i = 0; i < length; i++) {
            text[i] = SOURCES.charAt(random.nextInt(SOURCES.length()));
        }
        return new String(text);
    }

    public static String generateNumber(Random random, int length) {
        char[] text = new char[length];
        for (int i = 0; i < length; i++) {
            text[i] = NUM_SOURCES.charAt(random.nextInt(NUM_SOURCES.length()));
        }
        return new String(text);
    }
}
