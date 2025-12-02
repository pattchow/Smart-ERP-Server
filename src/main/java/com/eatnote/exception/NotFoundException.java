package com.eatnote.exception;

/**
 *
 */
public class NotFoundException extends RuntimeException {

    private final static String message = "data not found";

    public NotFoundException(String message) {
        super(message);
    }

    public NotFoundException() {
        super(message);
    }
}
