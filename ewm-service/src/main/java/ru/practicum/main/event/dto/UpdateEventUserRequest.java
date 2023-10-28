package ru.practicum.main.event.dto;

import lombok.*;
import ru.practicum.main.location.model.Location;

import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UpdateEventUserRequest {
    @Size(min = 20)
    @Size(max = 2000)
    private String annotation;

    @Positive
    private Long category;

    @Size(min = 20)
    @Size(max = 7000)
    private String description;
    private String eventDate;
    private Location location;
    private Boolean paid;

    @Positive
    private Integer participantLimit;

    private Boolean requestModeration;
    private String stateAction;

    @Size(min = 3)
    @Size(max = 120)
    private String title;
}