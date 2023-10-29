package ru.practicum.main.exception;

public class ValidationEventException extends RuntimeException {
    public ValidationEventException(String message) {
        super(message);
    }
}