package org.god.godecommerce.payload;

import java.util.List;

public record ProductResponse(
        List<ProductDTO> content,
        Integer page,
        Integer pageSize,
        Long totalElements,
        Integer totalPages,
        boolean isLastPage
) {
}
