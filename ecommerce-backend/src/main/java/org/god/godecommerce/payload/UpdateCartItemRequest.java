package org.god.godecommerce.payload;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;

public record UpdateCartItemRequest(
        @Min(value = 0, message = "quantity should not be less than 0")
        Integer quantity) {
}
