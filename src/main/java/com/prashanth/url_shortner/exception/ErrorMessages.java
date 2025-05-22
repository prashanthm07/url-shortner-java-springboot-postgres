package com.prashanth.url_shortner.exception;

import org.springframework.stereotype.Component;

public class ErrorMessages {
    public static final String URL_NOT_FOUND = "URL not found";
    public static final String URL_EXPIRED = "URL has expired";
    public static final String URL_NOT_ACTIVE = "URL is not active";
    public static final String URL_FORMAT_INVALID = "URL format is not valid";
    public static final String URL_EXPIRY_INVALID = "URL expiry date is not valid";
}
