package com.runtime.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import com.runtime.entity.Template;
import com.runtime.repository.TemplateRepository;

import reactor.core.publisher.Mono;

@Service
public class TemplateService {

	@Autowired
	private TemplateRepository templateRepository;

	public Mono<Template> save(Template template) {

		return templateRepository.save(template);
	}

	public Mono<Template> setRunTimeByUserUpdate(@RequestBody Template update) {
		return templateRepository.findById(update.getId()).flatMap(userEmail -> {
			userEmail.setRunTime(update.getRunTime());
			userEmail.setToAddress(update.getToAddress());
			userEmail.setJobStatus("S");
			return templateRepository.save(userEmail);
		});
	}

}
