package com.example.oauth.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public class User {
    @JsonProperty("id")
    private int id;

    @JsonProperty("email")
    private String email;

    @JsonProperty("username")
    private String userName;

    @JsonProperty("password")
    private String password;

    @JsonProperty("name")
    private Name name;

    @JsonProperty("phone")
    private String phone;

    @JsonProperty("address")
    private Address address;
}
