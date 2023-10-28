package ru.practicum.main.exception;

public class DuplicateParticipationException extends RuntimeException {
    public DuplicateParticipationException(String message) {
        super(message);
    }
}