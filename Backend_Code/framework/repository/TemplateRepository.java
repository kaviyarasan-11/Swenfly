package com.framework.repository;

import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;

import com.framework.entity.Template;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;


public interface TemplateRepository extends ReactiveCrudRepository <Template , String>{

	Mono<Template> save(Template template);

	@Query(value = "{'jobStatus': 'D'}")
	Flux<Template> findByJobStatus(String string);


}
