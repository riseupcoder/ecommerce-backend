package org.god.godecommerce.payload;

import java.util.List;

public record OrderResponse(
        List<OrderDTO> orders,
        Integer page, Integer pageSize,
        Long totalElements, Integer totalPages, boolean isLastPage
) {
}
