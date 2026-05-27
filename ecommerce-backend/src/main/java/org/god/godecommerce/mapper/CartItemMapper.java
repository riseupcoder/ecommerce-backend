package org.god.godecommerce.mapper;

import org.god.godecommerce.model.CartItem;
import org.god.godecommerce.payload.CartItemDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = {ProductMapper.class})
public interface CartItemMapper {
    @Mapping(target = "cartItemId", ignore = true)
    CartItem toEntity(CartItemDTO cartItemDTO);

    @Mapping(source = "product", target = "productDTO")
    CartItemDTO toDto(CartItem cartItem);
}
