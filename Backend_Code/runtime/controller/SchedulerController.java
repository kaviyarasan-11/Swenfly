package com.runtime.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import com.runtime.entity.Template;
import com.runtime.repository.TemplateRepository;
import com.runtime.service.TemplateService;

import reactor.core.publisher.Mono;

@RestController
public class SchedulerController {

	private TemplateRepository templateRepository;

	@Autowired
	private TemplateService templateService;

	@PutMapping("/set-run-update")
	public Mono<Template> setRunTimeByUserUpdate(@RequestBody Template update) {
		return templateService.setRunTimeByUserUpdate(update);
	}

}
