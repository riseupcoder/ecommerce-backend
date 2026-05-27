package org.god.godecommerce.service;

import org.god.godecommerce.payload.CategoryDTO;
import org.god.godecommerce.payload.CategoryResponse;
import org.springframework.data.domain.Pageable;

public interface CategoryService {
    CategoryResponse getAllCategories(Pageable pageable);

    CategoryDTO createCategory(CategoryDTO categoryDTO);

    CategoryDTO updateCategory(Long categoryId, CategoryDTO categoryDTO);

    CategoryDTO deleteCategory(Long categoryId);
}
