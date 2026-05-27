package org.god.godecommerce.util;

import org.god.godecommerce.model.User;
import org.god.godecommerce.repository.UserRepository;
import org.god.godecommerce.security.jwt.services.UserDetailsImpl;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

@Component
public class AuthUtil {
    private final UserRepository userRepository;

    public AuthUtil(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public String loggedInEmail() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication != null) {
            UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
            User user = userRepository.findById(userDetails.id())
                    .orElseThrow(() -> new UsernameNotFoundException("User not found with userId: " + userDetails.id()));
            return user.getEmail();
        }
        return null;
    }


    public Long loggedInUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication != null) {
            UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
            User user = userRepository.findById(userDetails.id())
                    .orElseThrow(() -> new UsernameNotFoundException("User not found with userId: " + userDetails.id()));
            return user.getId();
        }
        return null;
    }


    public User loggedInUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication != null) {
            UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
            return userRepository.findById(userDetails.id())
                    .orElseThrow(() -> new UsernameNotFoundException("User not found with userId: " + userDetails.id()));
        }
        return null;
    }
}
