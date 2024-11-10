package com.users.api.config;

import com.users.api.repository.UserRepository;
import com.users.api.service.JwtTokenService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final String[] AUTH_WHITELIST = {
            "/swagger-ui/**",
            "/v3/api-docs/**",
            "/swagger-ui.html",
            "/api/auth/**"
    };

    private final UserRepository userRepository;
    private final JwtTokenService jwtTokenService;

    public SecurityConfig(UserRepository userRepository, JwtTokenService jwtTokenService) {
        this.userRepository = userRepository;
        this.jwtTokenService = jwtTokenService;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .cors(corsConfig -> {})
                .csrf(AbstractHttpConfigurer::disable)
                .headers(headersConfig -> {
                    headersConfig.xssProtection(xXssConfig -> {});
                    headersConfig.cacheControl(cacheControlConfig -> {});
                })
                .authorizeHttpRequests(authConfig -> {
                    authConfig.requestMatchers(AUTH_WHITELIST).permitAll();
                    authConfig.requestMatchers("/admin/**").hasAnyAuthority("ROLE_ADMIN");
                    authConfig.anyRequest().authenticated();
                })
                .sessionManagement(sessionConfig ->
                    sessionConfig.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )
                .addFilterBefore(new UserAuthenticationFilter(jwtTokenService, userDetailsService()),
                        UsernamePasswordAuthenticationFilter.class)
                .exceptionHandling(exceptionConfig ->
                    exceptionConfig.authenticationEntryPoint(new CustomAuthenticationEntryPoint())
                )
                .httpBasic(httpConfig -> {});

        return http.build();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig) throws Exception {
        return authConfig.getAuthenticationManager();
    }

    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService());
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
    }

    @Bean
    public UserDetailsService userDetailsService() {
        return username -> userRepository.findByEmail(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
