package ru.practicum.main.exception;

public class OverflowLimitException extends RuntimeException {
    public OverflowLimitException(String message) {
        super(message);
    }
}