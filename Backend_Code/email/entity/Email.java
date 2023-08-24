package com.email.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Document(value = "email")
public class Email {
	
	@Id
	private String id;
	private String fromAddress;
	private String[] toAddress;
	private String[] cc;
	private String[] bcc;
	private String subject;
	private String body;
	
    private byte[] attachment;
    
    private Integer otp; //otp used for OTP email verification
}

