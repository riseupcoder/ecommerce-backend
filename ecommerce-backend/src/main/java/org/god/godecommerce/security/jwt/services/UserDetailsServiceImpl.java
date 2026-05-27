package org.god.godecommerce.security.jwt.services;

import org.god.godecommerce.model.User;
import org.god.godecommerce.repository.UserRepository;
import org.jspecify.annotations.NonNull;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserRepository userRepository;

    public UserDetailsServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @NonNull
    @Override
    @Transactional
    public UserDetailsImpl loadUserByUsername(@NonNull String username) throws UsernameNotFoundException {
        User user = userRepository.findByUserName(username);

        if (user == null)
            throw new UsernameNotFoundException(
                    "User not found with username: " + username);
        return UserDetailsImpl.build(user);
    }
}
