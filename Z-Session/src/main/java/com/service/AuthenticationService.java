package com.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import com.bean.LoginRequest;

import reactor.core.publisher.Mono;

@Service
public class AuthenticationService {

    private final WebClient webClient;

    @Value("${customerService.url}") // Fetch customer-service URL from application.properties
    private String customerServiceUrl;

    public AuthenticationService(WebClient.Builder webClientBuilder) {
        this.webClient = webClientBuilder.build();
    }

    public boolean authenticate(String email, String password) {
        // Create a LoginRequest object and set the email and password
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setEmail(email);
        loginRequest.setPassword(password);
    
        // Call customer-service for authentication
        Mono<Boolean> result = webClient.post()
            .uri(customerServiceUrl + "/authenticate")  // Assuming this is the correct URL in your customer service
            .bodyValue(loginRequest)  // Send the loginRequest object containing email and password
            .retrieve()
            .bodyToMono(Boolean.class);  // Expect a boolean response (true/false)
    
        return result.block();  // Blocking call for simplicity (not recommended for high-performance apps)
    }
    

}
