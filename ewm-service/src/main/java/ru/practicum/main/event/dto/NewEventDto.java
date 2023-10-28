package ru.practicum.main.event.dto;

import lombok.*;
import ru.practicum.main.location.model.Location;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class NewEventDto {

    @NotNull
    @NotBlank
    @Size(min = 20)
    @Size(max = 2000)
    private String annotation;

    @Positive
    private Long category;

    @NotNull
    @NotBlank
    @Size(min = 20)
    @Size(max = 7000)
    private String description;

    @NotNull
    @NotBlank
    private String eventDate;

    @NotNull
    private Location location;

    private Boolean paid;

    private Integer participantLimit;

    private Boolean requestModeration;

    @NotNull
    @NotBlank
    @Size(min = 3)
    @Size(max = 120)
    private String title;
}