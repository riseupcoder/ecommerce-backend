package org.god.godecommerce.security.response;

public record AuthResult(
        UserInfoResponse userInfoResponse,
        String jwtCookie
) {
}
