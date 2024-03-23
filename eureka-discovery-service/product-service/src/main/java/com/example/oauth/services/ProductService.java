package com.example.oauth.services;


import com.example.oauth.dto.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Service
public class ProductService {

    @Autowired
    RestTemplate template;

    public List<Product> getProducts() {
        ResponseEntity<Product[]> response = template.getForEntity("https://fakestoreapi.com/products", Product[].class);
        Product[] products = response.getBody();
        return products != null ? Arrays.stream(products).toList() : List.of();
    }

    public Product getProduct(int id) {

        URI uri = UriComponentsBuilder.fromHttpUrl("https://fakestoreapi.com")
                .path("products/{productId}")
                .buildAndExpand(id)
                .toUri();
        System.out.println(uri);
        ResponseEntity<Product> response = template.getForEntity(uri.toString(), Product.class);
        Product product = response.getBody();
        if (product != null) {
            return product;
        }
        throw new RuntimeException("Product not found");
    }
}
