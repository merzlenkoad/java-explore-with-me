package ru.practicum.main.event.dto;

import lombok.*;
import ru.practicum.main.category.dto.CategoryDto;
import ru.practicum.main.user.dto.UserShortDto;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EventShortDto {

    private Long id;
    private String annotation;
    private CategoryDto category;
    private Long confirmedRequests;
    private String eventDate;
    private UserShortDto initiator;
    private Boolean paid;
    private String title;
    private Long views;
}