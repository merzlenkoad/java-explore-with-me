package ru.practicum.main;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = {"ru.practicum.main", "ru.practicum.stats"})
public class EwmServiceApp {
    public static void main(String[] args) {
        SpringApplication.run(EwmServiceApp.class, args);
    }
}
