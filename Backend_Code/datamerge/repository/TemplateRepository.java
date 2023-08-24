package com.datamerge.repository;

import org.springframework.data.mongodb.repository.Query;

import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;

import com.datamerge.entity.Template;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Repository
public interface TemplateRepository  extends ReactiveCrudRepository<Template , String>{

	@Query(value = "{'jobStatus': 'D'}")
	Flux<Template> findByJobStatus(String string);
	
	@Query(value = "{'jobStatus': 'R'}")
	Flux<Template> findByJob(String string);
	
	@Query(value = "{'jobStatus': 'TC'}")
	Flux<Template> findByJobStat(String string);
	

	
	
}
