package com.email.entity;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EmailDto {
	
	
	private String fromAddress;
	private String[] toAddress;
	private String[] cc;
	private String[] bcc;
	private String subject;
	private String templateName;
	private String templateValue;

}