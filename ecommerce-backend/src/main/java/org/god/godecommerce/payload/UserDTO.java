package org.god.godecommerce.payload;

public record UserDTO(
        Long userId,
        String userName,
        String email
) {
}
