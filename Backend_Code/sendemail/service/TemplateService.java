package com.sendemail.service;

import java.util.Properties;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.springframework.ui.freemarker.FreeMarkerConfigurationFactoryBean;

import com.sendemail.entity.Template;
import com.sendemail.repository.TemplateRepository;

import org.springframework.mail.javamail.JavaMailSenderImpl;
import reactor.core.publisher.Mono;

@Service
public class TemplateService {
	
	@Autowired
	private TemplateRepository templateRepository;


	public Mono<Template> save(Template template) {
		
		return templateRepository.save(template);
	}
	
	@Bean
	public JavaMailSender getJavaMailSender() {
	    JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
	    mailSender.setHost("email-smtp.ap-south-1.amazonaws.com");
	    mailSender.setPort(587);
	    
	    mailSender.setUsername("AKIAWCGWCSOYZHCJFK2V");
	    mailSender.setPassword("BNxt9rjm1kYZvRpMQhFSrt5qsF09dPQXJId/l/8E8KiG");
	    
	    Properties props = mailSender.getJavaMailProperties();
	    props.put("mail.transport.protocol", "smtp");
	    props.put("mail.smtp.auth", "true");
	    props.put("mail.smtp.starttls.enable", "true");
	    props.put("mail.debug", "true");
	    
	    return mailSender;
	}
	 @Bean
	    public FreeMarkerConfigurationFactoryBean getFreeMarkerConfiguration() {
	        FreeMarkerConfigurationFactoryBean fmConfigFactoryBean = new FreeMarkerConfigurationFactoryBean();
	        fmConfigFactoryBean.setTemplateLoaderPath("/templates/");
	        return fmConfigFactoryBean;
	    }


	
	
}
