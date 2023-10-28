package ru.practicum.main.category.service;

import ru.practicum.main.category.dto.CategoryDto;
import ru.practicum.main.category.dto.NewCategoryDto;

import java.util.List;

public interface CategoryService {
    CategoryDto addCategoryAdmin(NewCategoryDto newCategoryDto);

    CategoryDto updateCategoryAdmin(Long catId, NewCategoryDto newCategoryDto);

    void deleteCategoryAdmin(Long catId);

    List<CategoryDto> getCategoryPublic(Integer from, Integer size);

    CategoryDto getCategoryByIdPublic(Long catId);
}