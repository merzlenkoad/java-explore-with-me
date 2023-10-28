package ru.practicum.main.location.dto;

import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LocationDto {

    private Double lat;
    private Double lon;
}