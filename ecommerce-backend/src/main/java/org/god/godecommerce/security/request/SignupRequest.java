package org.god.godecommerce.security.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record SignupRequest(
        @NotBlank
        @Size(min = 3, max = 20, message = "username must contain at least 3 letters.")
        String userName,

        @NotBlank
        @Size(max = 30, message = "please enter a valid email address.")
        @Email
        String email,

        @NotBlank
        @Size(min = 8, max = 50, message = "password must contain at least 8 letters.")
        String password
) {
}
