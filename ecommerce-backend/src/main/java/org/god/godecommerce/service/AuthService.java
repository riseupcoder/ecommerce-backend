package org.god.godecommerce.service;

import org.god.godecommerce.model.AppRole;
import org.god.godecommerce.payload.UserResponse;
import org.god.godecommerce.security.request.LoginRequest;
import org.god.godecommerce.security.request.SignupRequest;
import org.god.godecommerce.security.response.AuthResult;
import org.god.godecommerce.security.response.MessageResponse;
import org.god.godecommerce.security.response.UserInfoResponse;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseCookie;
import org.springframework.security.core.Authentication;

public interface AuthService {
    AuthResult authenticateUser(LoginRequest loginRequest);

    MessageResponse registerUser(SignupRequest signupRequest, AppRole appRole);

    UserInfoResponse getUserDetails(Authentication authentication);

    ResponseCookie logOut();

    UserResponse getAllSellers(Pageable pageable);
}
