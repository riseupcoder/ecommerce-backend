package org.god.godecommerce.payload;

import org.god.godecommerce.model.OrderStatus;

public record OrderStatusUpdateDTO(
        Long orderId,
        OrderStatus orderStatus
) {
}
