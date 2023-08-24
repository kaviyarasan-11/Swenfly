package com.email.controller;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;


import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.email.entity.Task;
import com.email.repository.TaskRepositoty;

import reactor.core.publisher.Mono;

@RestController
public class TaskController {
	
	 private static final Logger LOGGER = LoggerFactory.getLogger(EmailController.class);

	
	@Autowired
    private AmazonS3 s3Client;
	
	@Autowired 
	private TaskRepositoty taskRepositoty;
	
	private static final String BUCKET_NAME = "emailfiles55";
	

	@RequestMapping(value = "/upload-aws", method = RequestMethod.POST)
	public Mono<String> uploadAttachment(@RequestPart("file") byte[] attachment, @RequestPart("text") String fileName) throws IOException {
	    
		Task task= new Task();
		task.setFileName(fileName);
		taskRepositoty.save(task).subscribe();
		System.out.println(fileName);
	    
	    try {
	        // read all lines of the file and count them
	        long count = new BufferedReader(new InputStreamReader(new ByteArrayInputStream(attachment)))
	                            .lines()
	                            .count();
	        System.out.println("Total Lines: " + count);

	    } catch (Exception e) {
	        e.printStackTrace();
	    }
	    LOGGER.info("Had file and file name..");
	   
        // create object metadata with the attachment size
        ObjectMetadata metadata = new ObjectMetadata();
        metadata.setContentLength(attachment.length);

        // upload the attachment to S3
        s3Client.putObject(BUCKET_NAME ,fileName, new ByteArrayInputStream(attachment), metadata);
        LOGGER.info("file uploaded to S3 ...");
        
        // Return response with task ID
        return Mono.just("Attachment uploaded to S3. Task ID : " + task.getId());
        
      
	}


         //set run time
		 @PostMapping("/set-run")
		 public Mono<Task> setRunTimeByUser(@RequestBody Task update) {
			 
		     return taskRepositoty.findById(update.getId())
		             .flatMap(userEmail -> {
		            	 userEmail.setFromAddress(update.getFromAddress());
		            	 userEmail.setSubject(update.getSubject());
		                 userEmail.setRunTime(update.getRunTime()); // Update the runTime field
		                 userEmail.setCampaign(update.getCampaign());
		                 userEmail.setTemplateName(update.getTemplateName());
		                 userEmail.setJobStatus("S");
		                 
		                 LOGGER.info("Run time value set ...");
		                 
		                 return taskRepositoty.save(userEmail); // Save the updated document
		                 
		             });
		 }
		 
		 //update run time
		 @PutMapping("/set-run-update")
		 public Mono<Task> setRunTimeByUserUpdate(@RequestBody Task update) {
			 
		     return taskRepositoty.findById(update.getId())
		             .flatMap(userEmail -> {
		            	 userEmail.setFromAddress(update.getFromAddress());
		            	 userEmail.setSubject(update.getSubject());
		                 userEmail.setRunTime(update.getRunTime()); // Update the runTime field
		                 userEmail.setCampaign(update.getCampaign());
		                 userEmail.setTemplateName(update.getTemplateName());
		                 userEmail.setJobStatus("S");
		                 
		                 LOGGER.info("Run time value updated ...");

		                 
		                 return taskRepositoty.save(userEmail); // Save the updated document
		             });
		 }

}
