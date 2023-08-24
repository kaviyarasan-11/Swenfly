package com.datamerge.controller;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.InputStreamSource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.RestController;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.GetObjectRequest;
import com.amazonaws.services.s3.model.S3Object;
import com.datamerge.repository.TemplateRepository;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import reactor.core.publisher.Mono;

@RestController
public class SendEmailController {


	@Autowired
	private TemplateRepository templateRepository;

	@Autowired
	private JavaMailSender emailSender;

	
	@Scheduled(cron = "0 * * * * *")
	public void sendEmailWithAttachment() {
		templateRepository.findByJobStat("TC").flatMap(task -> {
			try {
				MimeMessage sender = emailSender.createMimeMessage();
				MimeMessageHelper message = new MimeMessageHelper(sender, true);
				message.setFrom("bals@swenfly.com");
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
