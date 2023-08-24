package com.framework.entity;

import java.time.LocalDateTime;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Document(value = "template")
public class Template {

	@Id
	private String id;
	// private String fromAddress;
	private String[] toAddress;
	private String jobStatus; // FU - File uploaded ,S - Scheduled, D - Downloaded, R - Running, TC - task
								// completed, JD - Job Done
	private byte[] attachment;
	private String fileName;
	private LocalDateTime runTime;

}