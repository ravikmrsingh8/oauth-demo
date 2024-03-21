package com.example.oauth;


import com.example.oauth.dto.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;

@Service
public class ProductService {

    @Autowired
    RestTemplate template;

    public List<Product> getProducts() {
        ResponseEntity<Product[]> response = template.getForEntity("https://fakestoreapi.com/products", Product[].class);
        Product[] products = response.getBody();
        return products != null ? Arrays.stream(products).toList() : List.of();
    }
}
