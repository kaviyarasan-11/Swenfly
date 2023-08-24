package com.sendemail.repository;

import org.springframework.data.mongodb.repository.Query;


import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Repository;
import com.sendemail.entity.Template;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Repository
public interface TemplateRepository  extends ReactiveCrudRepository<Template , String>{

	@Query(value = "{'jobStatus': 'TC'}")
	Flux<Template> findByJobStatus(String string);
	

}
