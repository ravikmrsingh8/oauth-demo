package com.example.oauth.services;


import com.example.oauth.dto.User;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
public class UserService {
    private final RestTemplate template;

    public UserService(RestTemplate template) {
        this.template = template;
    }

    public List<User> getUsers() {
        ResponseEntity<User[]> response = template.getForEntity("https://fakestoreapi.com/users", User[].class);
        User[] users = response.getBody();
        return users != null ? Arrays.stream(users).toList() : new ArrayList<>();
    }
}
