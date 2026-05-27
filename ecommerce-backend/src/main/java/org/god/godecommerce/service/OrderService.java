package org.god.godecommerce.service;

import org.god.godecommerce.payload.OrderDTO;
import org.god.godecommerce.payload.OrderRequestDTO;
import org.god.godecommerce.payload.OrderResponse;
import org.god.godecommerce.payload.OrderStatusUpdateDTO;
import org.springframework.data.domain.Pageable;

public interface OrderService {
    OrderDTO placeOrder(String email, OrderRequestDTO orderRequestDTO);
    OrderResponse getAllOrders(Pageable pageable);

    OrderDTO updateOrderStatus(OrderStatusUpdateDTO request);

    OrderResponse getSellerOrders(Long userId, Pageable pageable);
}
