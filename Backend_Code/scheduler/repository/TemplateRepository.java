package com.scheduler.repository;

import org.springframework.data.mongodb.repository.Query;

import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import com.scheduler.entity.Template;

import reactor.core.publisher.Flux;

public interface TemplateRepository extends ReactiveCrudRepository<Template, String> {

	@Query(value = "{'jobStatus': 'S'}")
	Flux<Template> findByJobStatus(String string);

}
