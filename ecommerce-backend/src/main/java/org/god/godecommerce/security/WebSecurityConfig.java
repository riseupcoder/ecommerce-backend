package org.god.godecommerce.security;

import org.god.godecommerce.model.AppRole;
import org.god.godecommerce.model.Role;
import org.god.godecommerce.model.User;
import org.god.godecommerce.repository.RoleRepository;
import org.god.godecommerce.repository.UserRepository;
import org.god.godecommerce.security.jwt.AuthEntryPointJwt;
import org.god.godecommerce.security.jwt.AuthTokenFilter;
import org.god.godecommerce.security.jwt.JwtUtils;
import org.god.godecommerce.security.jwt.services.UserDetailsServiceImpl;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class WebSecurityConfig {
    private final UserDetailsServiceImpl userDetailsService;
    private final AuthEntryPointJwt unauthorizedHandler;

    public WebSecurityConfig(UserDetailsServiceImpl userDetailsService, AuthEntryPointJwt authEntryPointJwt) {
        this.userDetailsService = userDetailsService;
        this.unauthorizedHandler = authEntryPointJwt;
    }

    @Bean
    public AuthTokenFilter authJwtTokenFilter(JwtUtils jwtUtils, UserDetailsServiceImpl userDetailsService) {
        return new AuthTokenFilter(jwtUtils, userDetailsService);
    }

    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authenticationProvider =
                new DaoAuthenticationProvider(userDetailsService);
        authenticationProvider.setPasswordEncoder(passwordEncoder());
        return authenticationProvider;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig) {
        return authConfig.getAuthenticationManager();
    }

    @Bean
    SecurityFilterChain filterChain(HttpSecurity http, AuthTokenFilter authJwtTokenFilter) {
        http
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/api/auth/**").permitAll()
                        .requestMatchers("/api/public/**").permitAll()
                        .requestMatchers("/api/orders/**").hasAnyRole("ADMIN", "SELLER", "USER")
                        .anyRequest().authenticated()
                )
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.NEVER))
                .csrf(AbstractHttpConfigurer::disable)
                .addFilterBefore(authJwtTokenFilter, UsernamePasswordAuthenticationFilter.class)
                .headers(headers -> headers.frameOptions(HeadersConfigurer.FrameOptionsConfig::sameOrigin))
                .exceptionHandling(exception -> exception
                        .authenticationEntryPoint(unauthorizedHandler))
                .authenticationProvider(authenticationProvider());

        return http.build();
    }

    @Bean
    public CommandLineRunner initData(UserRepository userRepository, RoleRepository roleRepository) {
        return args -> {
            Role admin_role = new Role(AppRole.ROLE_ADMIN);
            Role user_role = new Role(AppRole.ROLE_USER);
            Role seller_role = new Role(AppRole.ROLE_SELLER);

            roleRepository.save(admin_role);
            roleRepository.save(user_role);
            roleRepository.save(seller_role);

            User admin = new User("admin", "admin@email.com", passwordEncoder().encode("admin"));
            User user = new User("user", "user@email.com", passwordEncoder().encode("user"));
            User seller = new User("seller", "seller@email.com", passwordEncoder().encode("seller"));

            admin.getRoles().add(admin_role);
            user.getRoles().add(user_role);
            seller.getRoles().add(seller_role);

            userRepository.save(admin);
            userRepository.save(user);
            userRepository.save(seller);
        };
    }
}
