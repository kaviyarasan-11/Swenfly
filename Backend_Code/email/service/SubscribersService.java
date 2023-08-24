package com.email.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;

import com.email.entity.Subscribers;
import com.email.repository.SubscriberRepository;

import reactor.core.publisher.Mono;

@Service
public class SubscribersService {
    
    @Autowired
    private SubscriberRepository subscriberRepository;
    
   

	public Mono<Subscribers> save(Subscribers subscribers) {
		
		return subscriberRepository.save(subscribers);
	}


	public Mono<Subscribers> findByEmail(String emailAddress) {
		return subscriberRepository.findByEmail(emailAddress);
	}

	public boolean isSubscribed(String emailAddress) {
		return subscriberRepository.findByEmailAddress(emailAddress) != null;
	}
	public Mono<Subscribers> findByEmailAddress(String emailAddress) {
	    return subscriberRepository.findByEmailAddress(emailAddress);
	}



	
}

	

	





