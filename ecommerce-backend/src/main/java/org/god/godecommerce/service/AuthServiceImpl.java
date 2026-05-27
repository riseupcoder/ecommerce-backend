package org.god.godecommerce.service;

import org.god.godecommerce.exception.APIException;
import org.god.godecommerce.mapper.SellerMapper;
import org.god.godecommerce.model.AppRole;
import org.god.godecommerce.model.Role;
import org.god.godecommerce.model.User;
import org.god.godecommerce.payload.UserDTO;
import org.god.godecommerce.payload.UserResponse;
import org.god.godecommerce.repository.RoleRepository;
import org.god.godecommerce.repository.UserRepository;
import org.god.godecommerce.security.jwt.JwtUtils;
import org.god.godecommerce.security.jwt.services.UserDetailsImpl;
import org.god.godecommerce.security.request.LoginRequest;
import org.god.godecommerce.security.request.SignupRequest;
import org.god.godecommerce.security.response.AuthResult;
import org.god.godecommerce.security.response.MessageResponse;
import org.god.godecommerce.security.response.UserInfoResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseCookie;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class AuthServiceImpl implements AuthService {
    private final JwtUtils jwtUtils;
    private final AuthenticationManager authenticationManager;
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final SellerMapper sellerMapper;

    public AuthServiceImpl(JwtUtils jwtUtils, AuthenticationManager authenticationManager, UserRepository userRepository, RoleRepository roleRepository, PasswordEncoder passwordEncoder, SellerMapper sellerMapper) {
        this.jwtUtils = jwtUtils;
        this.authenticationManager = authenticationManager;
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
        this.sellerMapper = sellerMapper;
    }

    @Override
    public AuthResult authenticateUser(LoginRequest loginRequest) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginRequest.userName(), loginRequest.password()
                ));

        SecurityContextHolder.getContext().setAuthentication(authentication);
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        ResponseCookie jwtCookie = jwtUtils.generateJwtCookie(userDetails);
        List<String> roles = authentication.getAuthorities().stream().map(GrantedAuthority::getAuthority).toList();
        UserInfoResponse userInfoResponse = new UserInfoResponse(
                userDetails.id(), userDetails.userName(), roles);
        return new AuthResult(userInfoResponse, jwtCookie.toString());
    }

    @Override
    @Transactional
    public MessageResponse registerUser(SignupRequest signupRequest, AppRole appRole) {
        if (userRepository.existsByUserName(signupRequest.userName()))
            throw new APIException("Username already taken.");

        if (userRepository.existsByEmail(signupRequest.email()))
            throw new APIException("email already exists");

        User user = new User(signupRequest.userName(),
                signupRequest.email(),
                passwordEncoder.encode(signupRequest.password())
        );

        Role role = roleRepository.findByRoleName(appRole);
        user.getRoles().add(role);

        userRepository.save(user);

        String roleName = appRole.name().split("_")[1].toLowerCase();

        return new MessageResponse(roleName + " registered successfully.");
    }

    @Override
    public UserInfoResponse getUserDetails(Authentication authentication) {
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();

        if (userDetails == null) return null;

        List<String> roles = userDetails.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .toList();

        return new UserInfoResponse(
                userDetails.id(),
                userDetails.userName(),
                roles
        );
    }

    @Override
    public ResponseCookie logOut() {
        return jwtUtils.getCleanJwtCookie();
    }

    @Override
    public UserResponse getAllSellers(Pageable pageable) {
        Page<User> userPage = userRepository.findAllByRoleName(AppRole.ROLE_SELLER, pageable);

        List<UserDTO> userDTOS = userPage.getContent().stream()
                .map(sellerMapper::toDto)
                .toList();

        return new UserResponse(
                userDTOS,
                userPage.getNumber(),
                userPage.getSize(),
                userPage.getTotalElements(),
                userPage.getTotalPages(),
                userPage.isLast()
        );

    }
}
