package com.onarinskyi.exceptions;

public class UnknownBrowserException extends RuntimeException {
    public UnknownBrowserException(String message) {
        super(message);
    }
}
