package org.god.godecommerce.mapper;

import org.god.godecommerce.model.OrderItem;
import org.god.godecommerce.payload.OrderItemDTO;
import org.mapstruct.*;

@Mapper (componentModel = "spring")
public interface OrderItemMapper {
    @Mapping(target = "orderItemId", ignore = true)
    OrderItem toEntity(OrderItemDTO orderItemDTO);

    OrderItemDTO toDto(OrderItemMapper orderItem);
}
