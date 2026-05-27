package org.god.godecommerce.payload;

import java.util.List;

public record AddressResponse(
        List<AddressDTO> addresses, Integer page, Integer pageSize,
        Long totalElements, Integer totalPages, boolean isLastPage) {
}
