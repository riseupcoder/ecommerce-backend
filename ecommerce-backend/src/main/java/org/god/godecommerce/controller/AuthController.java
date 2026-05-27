package org.god.godecommerce.controller;

import jakarta.validation.Valid;
import org.god.godecommerce.model.AppRole;
import org.god.godecommerce.payload.UserResponse;
import org.god.godecommerce.security.jwt.services.UserDetailsImpl;
import org.god.godecommerce.security.request.LoginRequest;
import org.god.godecommerce.security.request.SignupRequest;
import org.god.godecommerce.security.response.AuthResult;
import org.god.godecommerce.security.response.MessageResponse;
import org.god.godecommerce.security.response.UserInfoResponse;
import org.god.godecommerce.service.AuthService;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.*;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'SELLER', 'USER')")
    @GetMapping("/username")
    public ResponseEntity<String> getCurrentUserName(
            @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return new ResponseEntity<>(userDetails.userName(), HttpStatus.OK);
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'SELLER', 'USER')")
    @GetMapping("/user")
    public ResponseEntity<UserInfoResponse> getUserDetails(Authentication authentication) {
        UserInfoResponse userInfoResponse = authService.getUserDetails(authentication);
        return new ResponseEntity<>(userInfoResponse, HttpStatus.OK);
    }

    @PostMapping("/signin")
    public ResponseEntity<UserInfoResponse> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {
        AuthResult authResult = authService.authenticateUser(loginRequest);

        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, authResult.jwtCookie())
                .body(authResult.userInfoResponse());
    }

    @PostMapping("/signup")
    public ResponseEntity<MessageResponse> registerUser(@Valid @RequestBody SignupRequest signupRequest) {
        MessageResponse messageResponse = authService.registerUser(signupRequest, AppRole.ROLE_USER);
        return new ResponseEntity<>(messageResponse, HttpStatus.OK);
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'SELLER', 'USER')")
    @PostMapping("/signout")
    public ResponseEntity<String> logOut() {
        ResponseCookie cookie = authService.logOut();
        return ResponseEntity.ok().header(HttpHeaders.SET_COOKIE, cookie.toString())
                .body("Logged out successfully.");
    }

    @PreAuthorize("hasAnyRole('ADMIN')")
    @GetMapping("/sellers")
    public ResponseEntity<UserResponse> getAllSellers(
            @PageableDefault() Pageable pageable
    ) {
        UserResponse allSellers = authService.getAllSellers(pageable);
        return new ResponseEntity<>(allSellers, HttpStatus.OK);
    }

    @PostMapping("/seller")
    public ResponseEntity<MessageResponse> registerSeller(
            @Valid @RequestBody SignupRequest signupRequest
    ) {
        MessageResponse status = authService.registerUser(signupRequest, AppRole.ROLE_SELLER);
        return new ResponseEntity<>(status, HttpStatus.CREATED);
    }
}
