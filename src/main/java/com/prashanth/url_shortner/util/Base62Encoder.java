package com.prashanth.url_shortner.util;

import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import java.util.Random;

public class Base62Encoder {

    private static final String BASE62 = "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";
    private static final int BASE = BASE62.length();
    private static final int SHORT_URL_LENGTH = 6; // 56800235584 = 56 Billion URLs
    private static final Random random = new Random();

    public static String encode(){
        StringBuilder encoded = new StringBuilder();
        for(int i=0;i<SHORT_URL_LENGTH;i++){
            int index = random.nextInt(BASE62.length());
            encoded.append(BASE62.charAt(index));
        }
        return encoded.toString();
    }
}
