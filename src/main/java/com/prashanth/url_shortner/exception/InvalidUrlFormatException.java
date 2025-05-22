package com.prashanth.url_shortner.exception;

public class InvalidUrlFormatException extends RuntimeException {
    public InvalidUrlFormatException(String message) {
        super(message);
    }
}
