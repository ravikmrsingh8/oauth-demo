package com.example.oauth.client.services;

import com.example.oauth.client.models.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientService;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
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
    private OAuth2AuthorizedClientService authorizedClientService;

    @Autowired
    RestClient restClient;

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
        URI uri = UriComponentsBuilder.fromHttpUrl(apiGatewayBaseUrl).path("/products/{id}").buildAndExpand(id).toUri();

        return restClient.get()
                .uri(uri)
                .headers(httpHeaders -> httpHeaders.addAll(headers))
                .retrieve()
                .body(Product.class);
    }

    private Product[] getProductsFromResourceServer() {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer "+ getAccessToken());

        URI uri = UriComponentsBuilder.fromHttpUrl(apiGatewayBaseUrl).path("/products").build().toUri();
        return restClient.get()
                .uri(uri)
                .headers(httpHeaders -> httpHeaders.addAll(headers))
                .retrieve()
                .body(Product[].class);
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
