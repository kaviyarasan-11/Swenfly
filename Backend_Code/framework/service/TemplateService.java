package com.framework.service;

import java.util.Properties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.springframework.ui.freemarker.FreeMarkerConfigurationFactoryBean;
import com.framework.entity.Template;
import com.framework.repository.TemplateRepository;

import org.springframework.mail.javamail.JavaMailSenderImpl;


import reactor.core.publisher.Mono;



@Service
public class TemplateService {
	
	@Autowired
	private TemplateRepository templateRepository;


	public Mono<Template> save(Template template) {
		
		return templateRepository.save(template);
	}



	
	
}
