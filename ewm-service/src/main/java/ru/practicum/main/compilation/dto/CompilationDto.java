package ru.practicum.main.compilation.dto;


import lombok.*;
import ru.practicum.main.event.dto.EventShortDto;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@EqualsAndHashCode
@AllArgsConstructor
public class CompilationDto {

    private Long id;
    private List<EventShortDto> events;
    private boolean pinned;
    private String title;
}