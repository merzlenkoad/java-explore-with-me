package ru.practicum.main.category.dto;

import lombok.*;

import javax.validation.constraints.Size;

@Data
@Builder
@NoArgsConstructor
@EqualsAndHashCode
@AllArgsConstructor
public class CategoryDto {

    private Long id;
    @Size(min = 1)
    @Size(max = 50)
    private String name;
}