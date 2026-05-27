package org.god.godecommerce.mapper;

import org.god.godecommerce.model.Category;
import org.god.godecommerce.payload.CategoryDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface CategoryMapper {
    @Mapping(target = "categoryId", ignore = true)
    Category toEntity(CategoryDTO categoryDTO);

    CategoryDTO toDto(Category category);
}
