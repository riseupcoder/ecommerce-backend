package org.god.godecommerce.payload;

import org.god.godecommerce.model.OrderStatus;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public record OrderDTO(
        Long orderId,
        String email,
        LocalDate orderDate,
        List<OrderItemDTO> orderItems,
        BigDecimal totalAmount,
        PaymentDTO paymentDTO,
        OrderStatus orderStatus,
        Long addressId
) {
}
