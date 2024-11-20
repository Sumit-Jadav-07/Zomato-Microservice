// package com.controller;

// import java.util.HashMap;
// import java.util.List;
// import java.util.Map;

// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.core.ParameterizedTypeReference;
// import org.springframework.web.bind.annotation.PostMapping;
// import org.springframework.web.bind.annotation.RequestBody;
// import org.springframework.web.bind.annotation.RequestMapping;
// import org.springframework.web.bind.annotation.RestController;
// import org.springframework.web.reactive.function.client.WebClient;

// import com.bean.LoginRequest;
// import com.bean.RestaurantEntity;
// import com.entity.CustomerEntity;
// import com.service.CustomerService;

// import reactor.core.publisher.Mono;

// @RestController
// @RequestMapping("api/public/session")
// public class SessionController {

//   @Autowired
//   private WebClient.Builder webClientBuilder;

//   private final String restaurantServiceUrl = "http://localhost:9696/api/restaurants";

//   @Autowired
//   CustomerService customerService;

//   @PostMapping("/login")
//   public Mono<Map<String, Object>> login(@RequestBody LoginRequest loginRequest) {

//     CustomerEntity customer = customerService.authenticateCustomer(loginRequest.getEmail());

//     Map<String, Object> response = new HashMap<>();

//     if (customer != null) {
//       WebClient webClient = webClientBuilder.baseUrl(restaurantServiceUrl).build(); // Build WebClient

//       return webClient.get() // Make the GET request
//           .retrieve() // Initiate the request
//           .bodyToMono(new ParameterizedTypeReference<List<RestaurantEntity>>() {
//           }) // Deserialize into List<RestaurantEntity>
//           .map(restaurants -> {
//             response.put("status", "success");
//             response.put("restaurants", restaurants); // Send the list of restaurants in response
//             return response;
//           })
//           .onErrorResume(e -> {
//             response.put("status", "error");
//             response.put("message", "Error fetching restaurants");
//             return Mono.just(response);
//           });
//     } else {
//       // Return error response if authentication fails
//       response.put("status", "error");
//       response.put("message", "Invalid email or password");
//       return Mono.just(response);
//     }
//   }

// }
