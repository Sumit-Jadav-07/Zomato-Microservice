package com.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.client.WebClient;

import com.bean.LoginRequest;
import com.bean.RestaurantEntity;
import com.service.AuthenticationService;

import reactor.core.publisher.Mono;

@RestController
@RequestMapping("api/public/session")
public class SessionController {

    @Autowired
    private WebClient.Builder webClientBuilder;

    @Value("${restaurantService.url}")
    private String restaurantServiceUrl;

    @Autowired
    private AuthenticationService authenticateService;

    @PostMapping("/login")
    public Mono<Map<String, Object>> login(@RequestBody LoginRequest loginRequest) {
        // Authenticate user
        Boolean isAuthenticated = authenticateService.authenticate(loginRequest.getEmail(), loginRequest.getPassword());
        System.out.println(isAuthenticated);
        Map<String, Object> response = new HashMap<>();

        if (isAuthenticated) {
            // If authenticated, fetch restaurant list
            WebClient webClient = webClientBuilder.baseUrl(restaurantServiceUrl).build();

            return webClient.get() // Make the GET request
                    .retrieve()
                    .bodyToMono(new ParameterizedTypeReference<List<RestaurantEntity>>() {}) // Correct usage
                    .map(restaurants -> {
                        response.put("status", "success");
                        response.put("restaurants", restaurants); // Add restaurant list to response
                        return response;
                    })
                    .onErrorResume(e -> {
                        response.put("status", "error");
                        response.put("message", "Error fetching restaurants");
                        return Mono.just(response);
                    });
        } else {
            // Authentication failed
            response.put("status", "error");
            response.put("message", "Invalid email or password");
            return Mono.just(response);
        }
    }
}
