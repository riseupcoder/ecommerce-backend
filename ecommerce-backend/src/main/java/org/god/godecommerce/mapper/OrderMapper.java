package org.god.godecommerce.mapper;

import org.god.godecommerce.model.CartItem;
import org.god.godecommerce.model.Order;
import org.god.godecommerce.model.Product;
import org.god.godecommerce.payload.OrderDTO;
import org.god.godecommerce.model.OrderItem;
import org.god.godecommerce.payload.ProductDTO;
import org.mapstruct.*;

@Mapper(componentModel = "spring", uses = {ProductMapper.class})
public interface OrderMapper {
    @Mapping(target = "orderId", ignore = true)
    Order toEntity(OrderDTO orderDTO);

    @Mapping(source = "address.addressId", target = "addressId")
    @Mapping(source = "payment", target = "paymentDTO")
    OrderDTO toDto(Order order);

    @Mapping(source = "productPrice", target = "orderedProductPrice")
    OrderItem cartItemToOrderItem(CartItem cartItem);
}
