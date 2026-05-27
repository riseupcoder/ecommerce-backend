package org.god.godecommerce.payload;

import java.math.BigDecimal;

public record CartItemDTO(ProductDTO productDTO, Integer quantity,
                          BigDecimal discount, BigDecimal productPrice) {
}
