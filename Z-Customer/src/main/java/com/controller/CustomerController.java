package com.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.client.WebClient;

import com.bean.LoginRequest;
import com.bean.RestaurantEntity;
import com.entity.CustomerEntity;
import com.repository.CustomerRepository;
import com.service.CustomerService;

import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/customers")
public class CustomerController {

	@Autowired
	CustomerRepository repo;

	@Autowired
	BCryptPasswordEncoder encoder;

	@Autowired
	private CustomerService customerService;

	@Autowired
	private WebClient.Builder webClientBuilder;

	@Value("${restaurantService.url}")
	private String restaurantServiceUrl;

	@PostMapping
	public String addCustomer(@RequestBody CustomerEntity entity) {
		entity.setPassword(encoder.encode(entity.getPassword()));
		repo.save(entity);
		return "Success";
	}

	@GetMapping
	public List<CustomerEntity> listCustomers() {
		List<CustomerEntity> customers = repo.findAll();
		return customers;
	}

	@GetMapping("{customerId}")
	public ResponseEntity<CustomerEntity> getCustomer(@PathVariable("customerId") Integer customerId) {
		Optional<CustomerEntity> op = repo.findById(customerId);
		if (op.isPresent()) {
			return ResponseEntity.ok(op.get());
		} else {
			return ResponseEntity.notFound().build();
		}
	}

	@DeleteMapping("{customerId}")
	public String deleteCustomer(@PathVariable("customerId") Integer customerId) {
		repo.deleteById(customerId);
		return "success";
	}

	@PutMapping
	public ResponseEntity<?> updateCustomer(@RequestBody CustomerEntity entity) {
		Optional<CustomerEntity> op = repo.findById(entity.getCustomerId());
		if (op.isEmpty()) {
			return ResponseEntity.ok("Invalid CustomerId");
		} else {
			repo.save(entity);
			return ResponseEntity.ok(entity);
		}
	}

	@PostMapping("/authenticate")
	public boolean authenticate(@RequestBody LoginRequest loginRequest) {
		// Validate email & password
		System.out.println(loginRequest.getEmail());
		System.out.println(loginRequest.getPassword());
		boolean isTrue = customerService.authenticateCustomer(loginRequest.getEmail(), loginRequest.getPassword());
		System.out.println(isTrue);
		return customerService.authenticateCustomer(loginRequest.getEmail(), loginRequest.getPassword());
	}

	@PostMapping("/newRestaurant")
	public Mono<ResponseEntity<String>> newRestaurant(@RequestBody RestaurantEntity restaurantEntity) {

		// WebClient ka use karke data bhejna
		return webClientBuilder.build()
				.post() // POST request banayi
				.uri(restaurantServiceUrl) // URL set kiya
				.bodyValue(restaurantEntity) // Body me data attach kiya
				.retrieve() // Response fetch karna start kiya
				.toEntity(String.class) // Response ko String me convert kiya
				.onErrorResume(e -> {
					// Agar koi error aaye, to handle karenge
					return Mono.just(ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
							.body("Error calling restaurant-service: " + e.getMessage()));
				});
	}

	@DeleteMapping("/deleteRestaurant/{restaurantId}")
	public ResponseEntity<?> deleteRestaurant(@PathVariable("restaurantId") Integer restaurantId) {
		// WebClient ka use karte hue restaurant-service ke delete API ko call karenge
		String response = webClientBuilder.build() // WebClient ka instance banaya
				.delete() // DELETE request specify kiya
				.uri(restaurantServiceUrl + "/" + restaurantId) // Restaurant-service ke DELETE API ka URL diya hai)
				.retrieve() // Response retrieve karna start kiya
				.bodyToMono(String.class) // Response ko String type Mono me convert kiya
				.block(); // Synchronous call ke liye block kiya

		// Response ko frontend ya client tak forward karenge
		return ResponseEntity.ok(response);
	}

	@PutMapping("/updateRestaurant/{restaurantId}")
	public ResponseEntity<?> updateRestaurant(@PathVariable("restaurantId") Integer restaurantId, @RequestBody RestaurantEntity entity){

		String response = webClientBuilder.build()
				.put()
				.uri(restaurantServiceUrl + "/" + restaurantId)
				.bodyValue(entity)
				.retrieve()
				.bodyToMono(String.class)
				.block();

		return ResponseEntity.ok(response);

	}

}