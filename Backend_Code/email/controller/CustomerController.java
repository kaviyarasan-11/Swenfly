package com.email.controller;

import java.time.LocalDate;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.email.entity.Customer;
import com.email.service.CustomerService;

import reactor.core.publisher.Mono;

@RestController

public class CustomerController {

	@Autowired
	private CustomerService customerService;
	
	
	@RequestMapping(value = "/save-customer", method = RequestMethod.POST)
	public Mono<Customer> saveCustomer(@RequestBody Customer customer) throws Exception{
		
		  LocalDate currentDate = LocalDate.now();	
		  customer.setIsActive(true);
		System.out.println("welcome to swenfly");
		  return customerService.saveCustomerDetails(customer);

		
	}
	@RequestMapping(value = "/get-customer/{id}", method = RequestMethod.GET)
	public Mono<Customer> fetchCustomer(@PathVariable String id) throws Exception{
		
		
		  return customerService.getCustomerDetails(id);

		
	}
}
