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
  public CustomerEntity authenticateCustomer(String email){
    CustomerEntity customer = customerRepo.findByEmail(email);
    if(customer != null){
      return customer;
    } 
    return null;
  }

}
