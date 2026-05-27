package org.god.godecommerce.payload;

import java.util.List;

public record CategoryResponse(List<CategoryDTO> content, Integer page, Integer pageSize, Long totalElements,
                               Integer totalPages, boolean isLastPage) {
}
