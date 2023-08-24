package com.email.repository;

import org.springframework.data.repository.reactive.ReactiveCrudRepository;

import com.email.entity.Task;

import reactor.core.publisher.Mono;

public interface TaskRepositoty  extends ReactiveCrudRepository<Task , String>{

//	void save(String filename);
Mono<Task> save(Task task);


}
