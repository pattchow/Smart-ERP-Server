package com.eatnote.exception;

/**
 * Exception thrown when requested data is not found
 */
public class NotFoundException extends RuntimeException {

    private final static String MESSAGE = "Data not found";

    public NotFoundException(String message) {
        super(message);
    }

    public NotFoundException() {
        super(MESSAGE);
    }
}