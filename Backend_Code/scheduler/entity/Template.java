package com.scheduler.entity;

import java.time.LocalDateTime;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import com.fasterxml.jackson.annotation.JsonFormat;

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
								// completed, JD - JobDone
	private byte[] attachment;
	private String fileName;
	private LocalDateTime runTime;

}
