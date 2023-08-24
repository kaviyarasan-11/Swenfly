package com.email.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.email.entity.Customer;
import com.email.repository.CustomerRepository;

import reactor.core.publisher.Mono;

@Service
public class CustomerService {
	 
	@Autowired
	private CustomerRepository customerRepository;

	public Mono<Customer> saveCustomerDetails(Customer customer) {
		
		return customerRepository.save(customer);
	}
	public Mono<Customer> getCustomerDetails(String id) {
		
		return customerRepository.findById(id);
	}
}
