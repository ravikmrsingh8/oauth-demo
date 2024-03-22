package com.example.oauth.controllers;

import com.example.oauth.services.ProductService;
import com.example.oauth.dto.Product;
import com.netflix.appinfo.EurekaInstanceConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/products")
public class ProductController {

    @Autowired
    ProductService service;

    @Autowired
    EurekaInstanceConfig config;

    @GetMapping
    public ResponseEntity<List<Product>> getProducts() {
        HttpHeaders headers = new HttpHeaders();
        headers.add("X-Service-Instance", config.getInstanceId());
        headers.forEach((key, value)-> System.out.println("Key:" + key + "\n Value:"+ value));
        return new ResponseEntity<>(service.getProducts(), headers, HttpStatus.OK);
    }
}
