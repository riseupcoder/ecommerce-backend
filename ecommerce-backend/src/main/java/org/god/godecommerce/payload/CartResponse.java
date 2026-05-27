package org.god.godecommerce.payload;

import java.util.List;

public record CartResponse(
        List<CartDTO> carts,
        Integer page,
        Integer pageSize,
        Long totalElements,
        Integer totalPages,
        boolean isLastPage
) {
}
