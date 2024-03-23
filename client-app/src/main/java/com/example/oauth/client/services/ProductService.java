package com.example.oauth.client.services;

import com.example.oauth.client.models.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientService;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.Arrays;
import java.util.List;

@Service
public class ProductService {

    @Value("${api-gateway-base-url}")
    private String apiGatewayBaseUrl;

    @Autowired
    private OAuth2AuthorizedClientService authorizedClientService;

    @Autowired
    RestTemplate restTemplate;

    public List<Product> getProducts(){
        Product[] products = getProductsFromResourceServer();
        return Arrays.stream(products).toList();
    }

    public Product getProductById(int id) {
        return getProductByIdFromService(id);
    }

    private Product getProductByIdFromService(int id) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer "+ getAccessToken());
        HttpEntity<Void> httpEntity = new HttpEntity<>(headers);

        URI uri = UriComponentsBuilder.fromHttpUrl(apiGatewayBaseUrl).path("/products/{id}").buildAndExpand(id).toUri();
        ResponseEntity<Product> response = restTemplate.exchange(uri, HttpMethod.GET, httpEntity, Product.class);
        if (response.getStatusCode() == HttpStatus.OK) {
            return response.getBody();
        }
        throw new RuntimeException("Couldn't fetch product");
    }

    private Product[] getProductsFromResourceServer() {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer "+ getAccessToken());
        HttpEntity<Void> httpEntity = new HttpEntity<>(headers);

        URI uri = UriComponentsBuilder.fromHttpUrl(apiGatewayBaseUrl).path("/products").build().toUri();
        ResponseEntity<Product[]> response = restTemplate.exchange(uri, HttpMethod.GET, httpEntity, Product[].class);
        if (response.getStatusCode() == HttpStatus.OK) {
            return response.getBody();
        }
        throw new RuntimeException("Couldn't fetch products");
    }

    private String getAccessToken() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication instanceof OAuth2AuthenticationToken oauthToken) {
            OAuth2AuthorizedClient oAuth2Client = authorizedClientService.loadAuthorizedClient(oauthToken.getAuthorizedClientRegistrationId(), oauthToken.getName());
            return oAuth2Client.getAccessToken().getTokenValue();
        }
        throw new RuntimeException("Couldn't fetch access token");
    }
}
