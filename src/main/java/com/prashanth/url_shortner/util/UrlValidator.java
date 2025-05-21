package com.prashanth.url_shortner.util;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Arrays;

public class UrlValidator {

    public static boolean isValid(String url) {
        try{
            URL parsedUrl = new URL(url);
            String protocol = parsedUrl.getProtocol();
            return (protocol.equals("http") || protocol.equals("https")) && parsedUrl.getHost() != null;
        }catch (MalformedURLException exception){
            System.out.println(Arrays.toString(exception.getStackTrace()));
            return false;
        }
    }
}
