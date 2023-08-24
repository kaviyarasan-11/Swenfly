package com.email.repository;

import org.springframework.data.repository.reactive.ReactiveCrudRepository;

import com.email.entity.Customer;

public interface CustomerRepository extends ReactiveCrudRepository< Customer, String >{
	

}
