package com.example.demo.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

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
                    authConfig.requestMatchers("/public/**", "/swagger-ui/**", "/v3/api-docs/**").permitAll();
                    authConfig.anyRequest().authenticated();
                })
                .httpBasic(httpConfig -> {});

        return http.build();
    }

}
