package org.god.godecommerce.service;

import jakarta.transaction.Transactional;
import org.god.godecommerce.exception.APIException;
import org.god.godecommerce.exception.ResourceNotFoundException;
import org.god.godecommerce.mapper.CategoryMapper;
import org.god.godecommerce.model.Category;
import org.god.godecommerce.payload.CategoryDTO;
import org.god.godecommerce.payload.CategoryResponse;
import org.god.godecommerce.repository.CategoryRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoryServiceImpl implements CategoryService {
    private final CategoryRepository categoryRepository;
    private final CategoryMapper categoryMapper;

    CategoryServiceImpl(CategoryRepository categoryRepository, CategoryMapper categoryMapper) {
        this.categoryRepository = categoryRepository;
        this.categoryMapper = categoryMapper;
    }

    @Override
    public CategoryResponse getAllCategories(
            Pageable pageable) {
        Page<Category> categoryPage = categoryRepository.findAll(pageable);
        List<Category> categories = categoryPage.getContent();

        List<CategoryDTO> categoryDTOS = categories.stream()
                .map(categoryMapper::toDto)
                .toList();

        return new CategoryResponse(
                categoryDTOS,
                categoryPage.getNumber(),
                categoryPage.getSize(),
                categoryPage.getTotalElements(),
                categoryPage.getTotalPages(),
                categoryPage.isLast()
        );
    }

    @Override
    public CategoryDTO createCategory(CategoryDTO categoryDTO) {
        Category category = categoryMapper.toEntity(categoryDTO);

        if (categoryRepository.existsByCategoryName(category.getCategoryName()))
            throw new APIException("category with " + category.getCategoryName() + " already exists.");

        Category savedCategory = categoryRepository.save(category);
        return categoryMapper.toDto(savedCategory);
    }

    @Override
    @Transactional
    public CategoryDTO updateCategory(Long categoryId, CategoryDTO categoryDTO) {
        Category category = categoryMapper.toEntity(categoryDTO);
        Category existingCategory = categoryRepository.findById(categoryId).orElseThrow(() -> new ResourceNotFoundException("Category", "categoryId", categoryId));

        if (categoryRepository.existsByCategoryNameAndCategoryIdNot(category.getCategoryName(), categoryId)) {
            throw new APIException("category with " + category.getCategoryName() + " already exists.");
        }

        existingCategory.setCategoryName(category.getCategoryName());
        return categoryMapper.toDto(existingCategory);
    }

    @Override
    public CategoryDTO deleteCategory(Long categoryId) {
        Category category = categoryRepository.findById(categoryId).orElseThrow(() -> new ResourceNotFoundException("Category", "categoryId", categoryId));
        categoryRepository.delete(category);
        return categoryMapper.toDto(category);
    }
}
