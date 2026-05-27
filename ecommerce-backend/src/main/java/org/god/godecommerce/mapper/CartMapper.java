package org.god.godecommerce.mapper;

import org.god.godecommerce.model.Cart;
import org.god.godecommerce.payload.CartDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = {ProductMapper.class, CartItemMapper.class})
public interface CartMapper {
//    @Mapping(target = "cartId", ignore = true)
//    Cart toEntity(CartDTO cartDTO);

    @Mapping(source = "cartItems", target = "items")
    CartDTO toDto(Cart cart);
}
