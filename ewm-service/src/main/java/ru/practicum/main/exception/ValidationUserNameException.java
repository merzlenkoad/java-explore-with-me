package ru.practicum.main.exception;

public class ValidationUserNameException extends RuntimeException {
    public ValidationUserNameException(String message) {
        super(message);
    }
}
