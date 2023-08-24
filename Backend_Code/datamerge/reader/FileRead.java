package com.datamerge.reader;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDateTime;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.InputStreamSource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.GetObjectRequest;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.util.IOUtils;
import com.datamerge.entity.Template;
import com.datamerge.repository.TemplateRepository;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Component
public class FileRead {

	private static final Logger LOGGER = LoggerFactory.getLogger(FileRead.class);

	@Value("${token.template.endpoint}")
	private String tokenEndpoint;

	@Autowired
	private WebClient webClient;

	@Autowired
	private TemplateRepository templateRepository;

	@Autowired
	private JavaMailSender emailSender;

	@Autowired
	private AmazonS3 s3Client;

	private static final String responseBucketName = "emailfiles55/Framework/Framework-Output/";

	private Mono<Object> saveResponseToBucket(String response, String fileName) {
		byte[] responseBytes = response.getBytes();
		ObjectMetadata metadata = new ObjectMetadata();
		metadata.setContentLength(responseBytes.length);
		Template tasks = new Template();
		templateRepository.findByJob("R").flatMap(task -> {
			task.setJobStatus("TC");
			return templateRepository.save(task).then(Mono.just(task));
		}).subscribe();
		return Mono.fromRunnable(() -> s3Client.putObject(responseBucketName, fileName,
				new ByteArrayInputStream(responseBytes), metadata));
		// .flatMap(task -> sendEmailWithAttachment());

	}

	

	private Mono<String> sendTemplate(byte[] attachment, String fileName) {
		// Flux<Template> tasks = templateRepository.findByJob("R");
		return webClient.post().uri(tokenEndpoint)
				.body(BodyInserters.fromMultipartData("file", attachment).with("text", fileName)).retrieve()
				.bodyToMono(String.class)
				.flatMap(response -> saveResponseToBucket(response, fileName).thenReturn(response));
	}

	@Scheduled(cron = "0 * * * * *")
	public void readFile() {
		// Get the current local date and time
		LocalDateTime currentDateTime = LocalDateTime.now();
		System.out.println("Current DateTime: " + currentDateTime);

		Flux<Template> tasks = templateRepository.findByJobStatus("D");
		tasks.filter(task -> task.getRunTime() != null).flatMap(task -> {
			task.setJobStatus("R");
			return templateRepository.save(task).then(Mono.just(task));
		}).flatMap(task -> {
			try {
				File file = new File("D:\\" + task.getFileName());
				InputStream inputStream = new FileInputStream(file);
				// BufferedReader reader = new BufferedReader(new
				// InputStreamReader(inputStream));
				byte[] attachment = IOUtils.toByteArray(inputStream);
				return sendTemplate(attachment, task.getFileName()); // Pass 'task' and 'attachment' as parameters
			} catch (IOException e) {
				return Mono.empty();
			}
		}).subscribe();
	}
}
