package org.god.godecommerce.payload;

public record AddToCartRequest(Long productId, Integer productQuantity) {
}
