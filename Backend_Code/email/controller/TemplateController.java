package com.email.controller;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutionException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.GetObjectRequest;
import com.amazonaws.services.s3.model.S3Object;
import com.email.entity.EmailDto;
import com.email.entity.Subscribers;
import com.email.repository.SubscriberRepository;
import com.email.service.EmailService;

import jakarta.mail.MessagingException;
import jakarta.mail.Multipart;
import jakarta.mail.internet.MimeMessage;
import reactor.core.publisher.Mono;

@RestController
public class TemplateController {

	@Autowired
	private EmailService emailService;

	@Autowired
	private JavaMailSender emailSender;

	@Autowired
	private SubscriberRepository subscriberRepository;

	
	
	@PostMapping(value = "/otp-email")
	public void sendEmail(@RequestBody EmailDto emailDto) throws IOException, MessagingException {
		String dataFile = emailDto.getTemplateName();

		// download the attachment from S3
		AmazonS3 s3Client = AmazonS3ClientBuilder.standard().withRegion("ap-south-1").build();
		S3Object object = s3Client.getObject(new GetObjectRequest("emailfiles55", dataFile));

		String template = new String(object.getObjectContent().readAllBytes());
		String templateValue = emailDto.getTemplateValue();
		String[] templateValues = templateValue.split(",");

		// Format the email body with the template and template values
		String formattedBody = String.format(template, templateValues);

		// Create a MIME message
		MimeMessage message = emailSender.createMimeMessage();
		MimeMessageHelper tmsg = new MimeMessageHelper(message, true);
		tmsg.setFrom(emailDto.getFromAddress());
		tmsg.setTo(emailDto.getToAddress());
		tmsg.setCc(emailDto.getCc());
		tmsg.setBcc(emailDto.getBcc());
		tmsg.setSubject(emailDto.getSubject());
		tmsg.setText(formattedBody, true);
	
		String toAddress = Arrays.toString(emailDto.getToAddress());

		
		
Subscribers subscriber;
		try {
			 subscriber = subscriberRepository.findByEmail(toAddress).toFuture().get();
		
			if (toAddress.equals(subscriber.getEmailAddress())) {
				// the email address obtained from the emailDto object matches the email address
				// in the database

				if (subscriber.isSubscribed()) {
					// The email address obtained from the emailDto object matches the email address
					// in the database
					emailSender.send(message);
				} else

				{
					System.out.println("Unsubscribed Email id: " + toAddress);
				}
			} else {
				
				subscriber.setEmailAddress(toAddress);

				subscriber.setSubscribed(true);
				subscriberRepository.save(subscriber).subscribe();

				emailSender.send(message);

			}
		} catch (MailException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ExecutionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
