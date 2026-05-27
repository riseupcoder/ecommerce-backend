package org.god.godecommerce.controller;

import jakarta.validation.Valid;
import org.god.godecommerce.config.AppConstants;
import org.god.godecommerce.payload.CategoryDTO;
import org.god.godecommerce.payload.CategoryResponse;
import org.god.godecommerce.service.CategoryService;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class CategoryController {
    private final CategoryService categoryService;

    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @GetMapping("/public/categories")
    public ResponseEntity<CategoryResponse> getAllCategories(
            @PageableDefault(sort = AppConstants.SORT_CATEGORIES_BY) Pageable pageable) {
        CategoryResponse categories = categoryService.getAllCategories(pageable);
        return new ResponseEntity<>(categories, HttpStatus.OK);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/admin/category")
    public ResponseEntity<CategoryDTO> createCategory(@RequestBody @Valid CategoryDTO categoryDTO) {
        CategoryDTO createdCategoryDTO = categoryService.createCategory(categoryDTO);
        return new ResponseEntity<>(createdCategoryDTO, HttpStatus.CREATED);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/admin/category/{categoryId}")
    public ResponseEntity<CategoryDTO> updateCategory(@PathVariable @Valid Long categoryId, @RequestBody CategoryDTO categoryDTO) {
        CategoryDTO updatedCategoryDTO = categoryService.updateCategory(categoryId, categoryDTO);
        return new ResponseEntity<>(updatedCategoryDTO, HttpStatus.OK);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/admin/category/{id}")
    public ResponseEntity<CategoryDTO> deleteCategory(@PathVariable Long id) {
        CategoryDTO deletedCategoryDTO = categoryService.deleteCategory(id);
        return new ResponseEntity<>(deletedCategoryDTO, HttpStatus.OK);
    }
}
