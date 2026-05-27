package org.god.godecommerce.repository;

import org.god.godecommerce.model.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {
    boolean existsByCategoryNameAndCategoryIdNot(String categoryName, Long categoryId);
    boolean existsByCategoryName(String categoryName);
}
