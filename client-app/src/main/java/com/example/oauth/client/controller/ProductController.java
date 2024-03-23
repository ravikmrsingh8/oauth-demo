package com.example.oauth.client.controller;

import com.example.oauth.client.models.Product;
import com.example.oauth.client.services.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@Controller
public class ProductController {

    @Autowired
    ProductService productService;

    @GetMapping("/products")
    public String products(@AuthenticationPrincipal OidcUser principal,
                           Model model) {
        List<Product> products = productService.getProducts();
        model.addAttribute("products",products);
        model.addAttribute("userName", principal.getUserInfo().getFullName());
        return "products";
    }

    @GetMapping("/products/{id}")
    public String getProductById(@PathVariable("id") int id,
                                 @AuthenticationPrincipal OidcUser principal,
                                 Model model) {
        Product product = productService.getProductById(id);
        model.addAttribute("product", product);
        model.addAttribute("userName", principal.getUserInfo().getFullName());
        return "product-details";
    }
}
