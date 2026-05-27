package org.god.godecommerce.payload;

import java.util.List;

public record UserResponse(
        List<UserDTO> sellers,
        Integer page,
        Integer pageSize,
        Long totalElements,
        Integer totalPages,
        boolean isLastPage
) {
}
