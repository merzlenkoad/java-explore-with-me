package ru.practicum.main.controllers.admin;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.main.category.dto.NewCategoryDto;
import ru.practicum.main.category.service.CategoryService;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import javax.validation.constraints.Positive;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.OK;

@Slf4j
@Validated
@RestController
@AllArgsConstructor
@RequestMapping(path = "/admin/categories")
public class CategoryAdminController {

    private final CategoryService categoryService;

    @PostMapping
    public ResponseEntity<?> addCategoryAdmin(HttpServletRequest request,
                                              @Valid @RequestBody NewCategoryDto newCategoryDto) {
        log.info("Post request received: adding a new category.");
        return new ResponseEntity<>(categoryService.addCategoryAdmin(newCategoryDto), CREATED);
    }

    @PatchMapping("/{catId}")
    public ResponseEntity<?> updateCategoryAdmin(HttpServletRequest request,
                                           @Positive @PathVariable Long catId,
                                           @Valid @RequestBody NewCategoryDto newCategoryDto) {
        log.info("Patch request received: category update.");
        return new ResponseEntity<>(categoryService.updateCategoryAdmin(catId, newCategoryDto), OK);
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/{catId}")
    public void deleteCategoryAdmin(HttpServletRequest request,
                                    @Positive @PathVariable("catId") Long catId) {
        log.info("Delete request received: category delete.");
        categoryService.deleteCategoryAdmin(catId);
    }
}