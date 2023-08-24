package com.email.controller;

import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.InputStream;
import java.io.ByteArrayInputStream;
import java.io.BufferedReader;
import java.util.ArrayList;
import java.util.List;
import java.util.HashMap;
import java.util.Map;

import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.InputStreamSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.mail.javamail.JavaMailSender;
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
import com.email.dto.ShareEmailRequest;
import com.email.entity.Email;
import com.email.service.EmailService;

import freemarker.template.Configuration;
import jakarta.annotation.Resource;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;
 

@RestController
public class EmailController {


	@Autowired
	private EmailService emailService;

	@Autowired
	private JavaMailSender emailSender;
	
	 @Qualifier("getFreeMarkerConfiguration")
	  @Autowired
	  Configuration fmConfiguration;
	 
	@RequestMapping(value = "/send-email", method = RequestMethod.POST)
	public Mono<Email> sendMail(@RequestBody Email email) throws Exception {
       
		MimeMessage sender = emailSender.createMimeMessage();
		MimeMessageHelper message = new MimeMessageHelper(sender, true);
		
		message.setFrom(email.getFromAddress());
        message.setTo(email.getToAddress());
		message.setCc(email.getCc());
		message.setBcc(email.getBcc());
		message.setSubject(email.getSubject());
		message.setText(email.getBody(),true);
		
        emailSender.send(sender);
        
		return emailService.saveEmail(email);
	}
	

	@RequestMapping(value = "/send-email-attachment", method = RequestMethod.POST)
	public Mono<Email> sendEmailWithAttachment(@RequestBody Email email) throws Exception, IOException {

	    MimeMessage sender = emailSender.createMimeMessage();
	    MimeMessageHelper message = new MimeMessageHelper(sender, true);
	    message.setFrom(email.getFromAddress());
	    message.setTo(email.getToAddress());
	    message.setCc(email.getCc());
	    message.setBcc(email.getBcc());
	    message.setSubject(email.getSubject());
	    message.setText(email.getBody(),true);

	    // download the attachment from S3
	    AmazonS3 s3Client = AmazonS3ClientBuilder.standard().withRegion("ap-south-1").build();
	    S3Object object = s3Client.getObject(new GetObjectRequest("emailfiles55", "fish.webp"));
	    byte[] attachment = object.getObjectContent().readAllBytes();

	    // create an InputStreamSource from the attachment byte array
	    InputStreamSource attachmentStream = new ByteArrayResource(attachment);

	    // attach the file to the email message
	    message.addAttachment("fish.webp", attachmentStream);
	    System.out.println("Attachment done");
	    

	    emailSender.send(sender);
	    return emailService.saveEmail(email);
	}
	
	//Send Bulk mail from CSV (To address , First Name , Last Name , Date)
	  @RequestMapping(value = "/send-emails-via-csv", method = RequestMethod.POST)
	  public void uploadCsvFile(@RequestPart("file") byte[] file,
	                             @RequestPart("text") String filename) throws IOException, MessagingException {

	      // Read the CSV file
	      InputStream inputStream = new ByteArrayInputStream(file);
	      BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
	      List<String[]> csvData = new ArrayList<>();
	      String line;
	      while ((line = reader.readLine()) != null) {
	          String[] rowData = line.split(",");
	          csvData.add(rowData);
	      }
	      reader.close();

	      // Send email to each toAddress
	      for (String[] row : csvData) {
	          String toAddress = row[0];
	          String firstName = row[1];
	          String lastName = row[2];
	          String date = row[3];
	          
	          String body = "Dear " + firstName + "" + lastName +",\n\nThank you for reaching us. Our executive will contact you by "
	                                                                                  + date + "." ;
	         
	          MimeMessage message = emailSender.createMimeMessage();
	          MimeMessageHelper helper = new MimeMessageHelper(message);
	          helper.setFrom("Bals@swenfly.com");
	          helper.setTo(toAddress);
	          helper.setSubject("Help desk from swenfly");
	          helper.setText(body,true);
	          emailSender.send(message);
	      }
	  }
	
	@PostMapping("/share-via-email")
	public Mono<String> shareEmail(@RequestBody ShareEmailRequest request) {
	    String toAddress = request.getToAddress();
	    String longUrl = request.getLongUrl();
	    String shortUrl = request.getShortUrl();
	    
	    Map<String, Object> modelShare = new HashMap<>();
        modelShare.put("longUrl" , longUrl);
        modelShare.put("shortUrl", shortUrl);
          
	    return Mono.fromCallable(() -> {
	        MimeMessage message = emailSender.createMimeMessage();
	        MimeMessageHelper helper = new MimeMessageHelper(message, true);

	        helper.setFrom("Bals@swenfly.com");
//       helper.setTo(StringUtils.join(toAddress, ","));
	        helper.setTo(toAddress);
	        helper.setSubject("SWENfly: Your short listed Urls here!");
       
	        // get mail content from template
	        String text = ShareEmailTemplate(modelShare);
	        helper.setText(text, true);
	        emailSender.send(message);

	        return "Email sent successfully!";
	    }).subscribeOn(Schedulers.boundedElastic());
	}
	
	 public String ShareEmailTemplate(Map<String, Object> modelShare) {
	        StringBuffer content = new StringBuffer();

	        try {
	            content.append(FreeMarkerTemplateUtils.processTemplateIntoString(fmConfiguration.getTemplate("share.html"), modelShare));
	        } catch (Exception e) {
	            e.printStackTrace();
	        }
	        return content.toString();
	    }	
	 
	@RequestMapping(value = "/send-email-template", method = RequestMethod.POST)
	public Mono<Email> sendEmailTemplate(@RequestBody Email email) throws Exception, IOException {
	    MimeMessage mimeMessage = emailSender.createMimeMessage();
	    try {
	        Map<String, Object> model = new HashMap<>();
	        model.put("firstName", "Balaji");
	        model.put("lastName", "M");

	        MimeMessageHelper message = new MimeMessageHelper(mimeMessage, true);

	        message.setFrom(email.getFromAddress());
	        message.setTo(email.getToAddress());
	        message.setCc(email.getCc());
	        message.setBcc(email.getBcc());
	        message.setSubject(email.getSubject());
	        
	        // get mail content from template
	        String mailContent = getContentFromTemplate(model);
	        message.setText(mailContent, true);
	        email.setBody(mailContent);

	        emailSender.send(mimeMessage);
	    } catch (MessagingException e) {
	        e.printStackTrace();
	    }
	     Mono.just(email);
	     return emailService.saveEmail(email);

	}
	 public String getContentFromTemplate(Map<String, Object> model) {
	        StringBuffer content = new StringBuffer();

	        try {
	            content.append(FreeMarkerTemplateUtils.processTemplateIntoString(fmConfiguration.getTemplate("welcome.html"), model));
	        } catch (Exception e) {
	            e.printStackTrace();
	        }
	        return content.toString();

	 }	 
	 @RequestMapping(value = "/send-otp-email", method = RequestMethod.POST)
		public Mono<Email> sendOtpTemplate(@RequestBody Email email) throws Exception, IOException {
		    MimeMessage mimeMessage = emailSender.createMimeMessage();
		    try {
		    	
		    
		        Map<String, Object> model = new HashMap<>();
		        model.put("OTP",email.getOtp() );
		       
		        MimeMessageHelper message = new MimeMessageHelper(mimeMessage, true);

		        message.setFrom(email.getFromAddress());
		        message.setTo(email.getToAddress());
		        message.setCc(email.getCc());
		        message.setBcc(email.getBcc());
		        message.setSubject(email.getSubject());
		       
		        // get mail content from template
		        String mailContent = getOtpTemplate(model);
		        message.setText(mailContent, true);
		        email.setBody(mailContent);
       
		        emailSender.send(mimeMessage);
		        
		    } catch (MessagingException e) {
		        e.printStackTrace();
		    }
		     Mono.just(email);
		     return emailService.saveEmail(email);

		}
		 public String getOtpTemplate(Map<String, Object> model) {
		        StringBuffer content = new StringBuffer();

		        try {
		            content.append(FreeMarkerTemplateUtils.processTemplateIntoString(fmConfiguration.getTemplate("OTP.html"), model));
		        } catch (Exception e) {
		            e.printStackTrace();
		        }
		        return content.toString();
		    }	 
	}

