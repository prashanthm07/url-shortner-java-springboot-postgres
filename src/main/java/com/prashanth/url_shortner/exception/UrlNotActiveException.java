package com.prashanth.url_shortner.exception;

public class UrlNotActiveException extends RuntimeException {
    public UrlNotActiveException(String message) {
        super(message);
    }
}
