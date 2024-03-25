package com.example.oauth.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class WebSecurityConfig {

    @Bean
    SecurityFilterChain configure(HttpSecurity http) throws Exception {
        JwtAuthenticationConverter converter = new JwtAuthenticationConverter();
        converter.setJwtGrantedAuthoritiesConverter(new KeycloakRoleConverter());

        http.authorizeHttpRequests(auth -> auth.requestMatchers(HttpMethod.GET, "/users/status").hasAuthority("SCOPE_readuser"));
        http.authorizeHttpRequests(auth -> auth.requestMatchers(HttpMethod.GET, "/users/token").hasRole("developer"));
        http.authorizeHttpRequests(auth -> auth.anyRequest().authenticated());

        // This application is an OAuth2 resource server and accepts jwt tokens
        http.oauth2ResourceServer(oauth -> oauth.jwt(jwt -> jwt.jwtAuthenticationConverter(converter)));

        return http.build();
    }
}
