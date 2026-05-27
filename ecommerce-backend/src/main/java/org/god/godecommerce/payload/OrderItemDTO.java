package org.god.godecommerce.payload;

import java.math.BigDecimal;

public record OrderItemDTO(
        Long orderItemId,
        ProductDTO product,
        Integer quantity,
        BigDecimal discount,
        BigDecimal orderedProductPrice
) {
}
