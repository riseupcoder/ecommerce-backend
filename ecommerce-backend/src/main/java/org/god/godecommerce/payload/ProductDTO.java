package org.god.godecommerce.payload;

import java.math.BigDecimal;

public record ProductDTO(
        Long productId,
        String productName,
        String image,
        String description,
        Integer quantity,
        BigDecimal price,
        BigDecimal discount,
        BigDecimal specialPrice) {
}
