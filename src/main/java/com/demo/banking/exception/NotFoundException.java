package com.demo.banking.exception;

public class NotFoundException extends Exception {
    public NotFoundException(String type, Long id) {
        super("%s not found with id - %d".formatted(type, id));
    }
}
