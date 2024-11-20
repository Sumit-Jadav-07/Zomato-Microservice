package com.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.entity.CustomerEntity;
import com.repository.CustomerRepository;

import jakarta.servlet.http.HttpServletResponse;

@Service
public class CustomerService {

  @Autowired
  HttpServletResponse response;

  @Autowired
  BCryptPasswordEncoder encoder;

  @Autowired
  CustomerRepository customerRepo;

  // Checking if email exist or not
  public boolean authenticateCustomer(String email, String password) {
    CustomerEntity customer = customerRepo.findByEmail(email);
    if (customer != null) {
      String encryptedPassword = customer.getPassword();
      System.out.println("Comparing password: " + password + " with encrypted password: " + encryptedPassword);
      if (encoder.matches(password, encryptedPassword)) {
        return true;
      }
    } else {
      System.out.println("Customer not found for email: " + email);
    }
    return false;
  }

}
