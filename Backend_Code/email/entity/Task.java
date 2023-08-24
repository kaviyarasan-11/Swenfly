package com.email.entity;

import java.time.LocalDateTime;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Document(value = "task")
public class Task {
	
	@Id
	private String id;
	private String fromAddress;
	private String subject;
    private LocalDateTime runTime;
	private String templateName;
	private String campaign;
	private String fileName;
    private byte[] file;
    private String jobStatus; // S - scheduled , R - running , C - cancelled , D - done 

}
