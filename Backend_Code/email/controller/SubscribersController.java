package com.email.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.email.entity.Subscribers;
import com.email.service.SubscribersService;

import reactor.core.publisher.Mono;

@RestController
public class SubscribersController {

	@Autowired
    private SubscribersService subscriberService;
	
	@PostMapping("/new-subscriber")
	public Mono<Subscribers> checkSubscribers(@RequestBody Subscribers subscribers) {

	    Mono<Subscribers> existingSubscriber = subscriberService.findByEmail(subscribers.getEmailAddress());

	    return existingSubscriber.flatMap(foundSubscriber -> {
	     //  System.out.println(foundSubscriber.getEmailAddress());
	       //System.out.println(foundSubscriber.getId());
	       
	    	// If subscriber already exists, return a response indicating that
	        return Mono.just(foundSubscriber);
	    }).switchIfEmpty(Mono.defer(() -> {
	    	
	        // If subscriber does not exist, create a new one and save it
	        subscribers.setSubscribed(true);
	    	 return subscriberService.save(subscribers);
	    }));
	}    
	@PutMapping("/subscribe")
	public Mono<Subscribers> saveSubscribers(@RequestParam String emailAddress) {
		
			  return subscriberService.findByEmail(emailAddress)
			        .flatMap(subscriber -> {
			            subscriber.setSubscribed(true);
			            return subscriberService.save(subscriber);
			        });
	}
	
	@PutMapping("/unsubscribe")
	public Mono<Subscribers> unsubscribe(@RequestParam String emailAddress) {
	    return subscriberService.findByEmail(emailAddress)
	        .flatMap(subscriber -> {
	            subscriber.setSubscribed(false);
	            return subscriberService.save(subscriber);
	        });
	}

}
    
    
    
    
    
    



