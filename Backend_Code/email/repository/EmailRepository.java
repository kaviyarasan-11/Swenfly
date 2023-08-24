package com.email.repository;

import org.springframework.data.repository.reactive.ReactiveCrudRepository;

import com.email.entity.Email;

public interface EmailRepository extends ReactiveCrudRepository <Email , String>{

}
