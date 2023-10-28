package ru.practicum.main.exception;

public class ValidationCategoryException extends RuntimeException {
    public ValidationCategoryException(String message) {
        super(message);
    }
}