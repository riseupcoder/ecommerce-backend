package org.god.godecommerce.security.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import org.jspecify.annotations.NonNull;

public record LoginRequest(
        @NotBlank
        @Size (min = 3, max = 20, message = "username should not exceed 20 characters.")
        String userName,

        @NonNull
        @Size(max = 50, message = "password should not exceed 50 letters.")
        String password) {
}
