package ru.practicum.main.category.dto;

import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Data
@NoArgsConstructor
@EqualsAndHashCode
@AllArgsConstructor
public class NewCategoryDto {

    @NotBlank
    @Size(min = 1)
    @Size(max = 50)
    private String name;
}