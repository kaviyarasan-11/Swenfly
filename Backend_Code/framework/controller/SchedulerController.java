package com.framework.controller;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.framework.entity.Template;
import com.framework.repository.TemplateRepository;
import com.framework.service.TemplateService;
import reactor.core.publisher.Mono;

@RestController
public class SchedulerController {
	@Autowired
	private AmazonS3 s3Client;

	@Autowired
	private TemplateRepository templateRepository;

	@Value("${token.template.endpoint}")
	private String tokenEndpoint;

	private static final String BUCKET_NAME = "emailfiles55/Framework/";

	private static final String responseBucketName = "emailfiles55/Framework/Framework-Output/";

	@RequestMapping(value = "/upload-temp", method = RequestMethod.POST)
	public Mono<String> uploadAttachment(@RequestPart("file") byte[] attachment, @RequestPart("text") String fileName)
			throws IOException {
		Template temp = new Template();
		temp.setFileName(fileName);
		templateRepository.save(temp).subscribe();
		// create object metadata with the attachment size
		ObjectMetadata metadata = new ObjectMetadata();
		metadata.setContentLength(attachment.length);

		// upload the attachment to S3
		s3Client.putObject(BUCKET_NAME, fileName, new ByteArrayInputStream(attachment), metadata);
		temp.setJobStatus("FU");

		return Mono.just("File Uploaded :" + temp.getId());
	}	
}


//@RequestMapping(value = "/upload-temp", method = RequestMethod.POST)
//public Mono<String> uploadAttachment(@RequestPart("file") byte[] attachment, @RequestPart("text") String fileName)
//        throws IOException {
//    Template temp = new Template();
//    temp.setFileName(fileName);
//
//    // Calculate the SHA-256 checksum of the uploaded file
//    String sha256Checksum = calculateChecksum(attachment, "SHA-256");
//    temp.setSHA256Checksum(sha256Checksum);
//
//    templateRepository.save(temp).subscribe();
//
//    // create object metadata with the attachment size and SHA-256 checksum
//    ObjectMetadata metadata = new ObjectMetadata();
//    metadata.setContentLength(attachment.length);
//    metadata.addUserMetadata("SHA-256-Checksum", sha256Checksum);
//
//    // upload the attachment to S3
//    s3Client.putObject(BUCKET_NAME, fileName, new ByteArrayInputStream(attachment), metadata);
//
//    temp.setJobStatus("FU");
//
//    return Mono.just("File Uploaded: " + temp.getId());
//}
//
//private String calculateChecksum(byte[] data, String algorithm) {
//    try {
//        MessageDigest md = MessageDigest.getInstance(algorithm);
//        byte[] digest = md.digest(data);
//        StringBuilder sb = new StringBuilder();
//        for (byte b : digest) {
//            sb.append(String.format("%02x", b & 0xff));
//        }
//        return sb.toString();
//    } catch (NoSuchAlgorithmException e) {
//        // Handle the exception, for example, log it and return an empty string
//        e.printStackTrace();
//        return "";
//    }
