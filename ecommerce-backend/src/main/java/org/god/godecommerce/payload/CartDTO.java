package org.god.godecommerce.payload;

import java.math.BigDecimal;
import java.util.List;

public record CartDTO(
        BigDecimal totalPrice, List<CartItemDTO> items) {
}
