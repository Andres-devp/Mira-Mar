package com.example.demo.exception;

public class NotFoundException extends RuntimeException {
    private Long id;

    public NotFoundException(String message) {
        super(message);
    }

    public NotFoundException(String message, Long id) {
        super(message);
        this.id = id;
    }

    public Long getId() {
        return id;
    }
}
