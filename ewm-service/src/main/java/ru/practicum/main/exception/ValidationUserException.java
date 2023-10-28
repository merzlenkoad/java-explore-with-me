package ru.practicum.main.exception;

public class ValidationUserException extends RuntimeException {
    public ValidationUserException(String message) {
        super(message);
    }
}
