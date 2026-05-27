package org.god.godecommerce.mapper;

import org.god.godecommerce.model.Product;
import org.god.godecommerce.payload.ProductDTO;
import org.mapstruct.*;

@Mapper (componentModel = "spring")
public interface ProductMapper {
    @Mapping(target = "productId", ignore = true)
    Product toEntity(ProductDTO productDTO);

    ProductDTO toDto(Product product);

    @Mapping(target = "productId", ignore = true)
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateEntityFromDto(ProductDTO productDTO, @MappingTarget Product product);
}
