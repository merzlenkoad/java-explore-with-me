package ru.practicum.main.compilation.dto;


import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@EqualsAndHashCode
@AllArgsConstructor
public class NewCompilationDto {

    private List<Long> events;
    private boolean pinned;

    @NotBlank
    @Size(min = 1)
    @Size(max = 50)
    private String title;
}