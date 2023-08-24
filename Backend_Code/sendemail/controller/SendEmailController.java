package com.sendemail.controller;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.InputStreamSource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RestController;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.GetObjectRequest;
import com.amazonaws.services.s3.model.S3Object;
import com.sendemail.entity.Template;
import com.sendemail.repository.TemplateRepository;
import com.sendemail.service.TemplateService;
import com.sendemail.webclient.WebFluxConfig;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import reactor.core.publisher.Mono;

@Component
public class SendEmailController {
	
	@Autowired
	private WebFluxConfig webclient;
	
	@Autowired
	private AmazonS3 s3Client;

	@Autowired
	private TemplateRepository templateRepository;

	@Autowired
	private JavaMailSender emailSender;

	private TemplateService emailService;

	//@Scheduled(cron = "0 * * * * *")
	
	//@RequestMapping(value = "/send-email", method = RequestMethod.POST)
	
	@Scheduled(cron = "0 * * * * *")
	public void sendEmailWithAttachment() {
		System.out.println("file attach");
		templateRepository.findByJobStatus("TC").flatMap(task -> {
			try {
				MimeMessage sender = emailSender.createMimeMessage();
				MimeMessageHelper message = new MimeMessageHelper(sender, true);
				message.setFrom("kavi11balakrishnan@gmail.com");
				message.setTo(task.getToAddress());
				message.setSubject("Task completed");
				message.setText("The given task completed");

				// download the attachment from S3
				AmazonS3 s3Client = AmazonS3ClientBuilder.standard().withRegion("ap-south-1").build();
				S3Object object = s3Client.getObject(
						new GetObjectRequest("emailfiles55/Framework/Framework-Output/", task.getFileName()));
				byte[] attachment = object.getObjectContent().readAllBytes();

				// create an InputStreamSource from the attachment byte array
				InputStreamSource attachmentStream = new ByteArrayResource(attachment);

				// attach the file to the email message
				message.addAttachment(task.getFileName(), attachmentStream);
				System.out.println("Attachment done");
				emailSender.send(sender);
				task.setJobStatus("JD");

				// Save and return the updated Task object
				return templateRepository.save(task);

			} catch (MessagingException | IOException e) {
				// Handle the exception appropriately
				return Mono.error(e);
			}
		}).subscribe();
	}

}
