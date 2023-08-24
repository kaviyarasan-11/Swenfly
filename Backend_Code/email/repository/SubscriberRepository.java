package com.email.repository;

import org.springframework.dao.DuplicateKeyException;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;

import com.email.entity.Subscribers;
import com.email.entity.Task;

import reactor.core.publisher.Mono;



public interface SubscriberRepository extends ReactiveCrudRepository<Subscribers , String> {

	
	
	@Query("{ 'emailAddress' : ?0 }")
	Mono<Subscribers> findByEmail(String emailAddress);
	


	 @Query("{ 'emailAddress' : ?0 }")
	 Mono<Subscribers> findByEmailAddress(String emailAddress);

	Mono<Subscribers> save(Task task);



	

}
	 

