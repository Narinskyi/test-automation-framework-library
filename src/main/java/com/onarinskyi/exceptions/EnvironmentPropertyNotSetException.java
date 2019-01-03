package com.onarinskyi.exceptions;

public class EnvironmentPropertyNotSetException extends RuntimeException {
    public EnvironmentPropertyNotSetException(String message) {
        super(message);
    }
}
