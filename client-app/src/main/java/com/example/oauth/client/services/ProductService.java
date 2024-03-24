package com.example.oauth.client.services;

import com.example.oauth.client.models.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.Arrays;
import java.util.List;

@Service
public class ProductService {

    @Value("${api-gateway-base-url}")
    private String apiGatewayBaseUrl;

    @Autowired
    RestClient restClient;

    public List<Product> getProducts(String accessToken){
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer "+ accessToken);

        URI uri = UriComponentsBuilder.fromHttpUrl(apiGatewayBaseUrl).path("/products").build().toUri();
        Product[] products =  restClient.get()
                .uri(uri)
                .headers(httpHeaders -> httpHeaders.addAll(headers))
                .retrieve()
                .body(Product[].class);

        return products != null ? Arrays.stream(products).toList() : List.of();
    }

    public Product getProductById(String accessToken, int id) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer "+ accessToken);
        URI uri = UriComponentsBuilder.fromHttpUrl(apiGatewayBaseUrl).path("/products/{id}").buildAndExpand(id).toUri();

        return restClient.get()
                .uri(uri)
                .headers(httpHeaders -> httpHeaders.addAll(headers))
                .retrieve()
                .body(Product.class);
    }
}
