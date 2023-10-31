package ru.practicum.main.constant;

import java.time.LocalDateTime;

public final class Constants {
    public static final String DATE_FORMAT = "yyyy-MM-dd HH:mm:ss";

    public static LocalDateTime timeNow() {
        return LocalDateTime.now();
    }
}