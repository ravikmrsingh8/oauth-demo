package com.example.oauth.controllers;

import com.example.oauth.dto.User;
import com.example.oauth.services.UserService;
import com.netflix.appinfo.EurekaInstanceConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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

    @Autowired
    EurekaInstanceConfig config;

    @GetMapping("/users")
    public ResponseEntity<List<User>> users(@AuthenticationPrincipal Jwt jwt) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("X-Service-Instance", config.getInstanceId());
        headers.forEach((key, value)-> System.out.println("Key:" + key + "\n Value:"+ value));
        System.out.println(Optional.of(jwt.getClaim("scope")));
        List<User> users = service.getUsers();
        return new ResponseEntity<>(users, headers, HttpStatus.OK);
    }
}
