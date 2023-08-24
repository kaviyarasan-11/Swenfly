package com.runtime.repository;

import org.springframework.data.repository.reactive.ReactiveCrudRepository;

import com.runtime.entity.Template;

import reactor.core.publisher.Mono;

public interface TemplateRepository extends ReactiveCrudRepository<Template, String> {

	Mono<Template> save(Template template);

}
