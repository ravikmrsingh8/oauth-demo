package com.example.oauth.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {
    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.authorizeHttpRequests(auth->auth.requestMatchers(HttpMethod.GET, "/products").hasAuthority("SCOPE_Read"));
        http.authorizeHttpRequests(auth -> auth.anyRequest().authenticated());
        http.oauth2ResourceServer(oauth -> oauth.jwt(jwt->{}));
        return http.build();
    }
}
