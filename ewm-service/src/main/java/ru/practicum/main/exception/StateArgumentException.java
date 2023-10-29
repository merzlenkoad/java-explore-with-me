package ru.practicum.main.exception;

public class StateArgumentException extends RuntimeException {
    public StateArgumentException(String message) {
        super(message);
    }
}