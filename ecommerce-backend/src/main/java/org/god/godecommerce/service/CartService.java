package org.god.godecommerce.service;

import org.god.godecommerce.model.Cart;
import org.god.godecommerce.payload.CartDTO;
import org.god.godecommerce.payload.CartResponse;
import org.springframework.data.domain.Pageable;

public interface CartService {
    CartDTO addToCart(Long productId, Integer quantity, Long userId);
    Cart createNewCart(Long userId);
    CartResponse getAllCarts(Pageable pageable);
    CartDTO getUserCart(Long userId);
    CartDTO updateItemQuantity(Long itemId, Integer quantity, Long userId);
    String deleteItemFromCart(Long itemId, Long userId);
}
