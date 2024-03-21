package com.example.oauth.controllers;

import com.example.oauth.dto.User;
import com.example.oauth.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;

@RestController
public class UserController {

    @Autowired
    UserService service;

    @GetMapping("/users")
    public List<User> users(@AuthenticationPrincipal Jwt jwt) {
        System.out.println(Optional.of(jwt.getClaim("scope")));
        return service.getUsers();
    }
}
