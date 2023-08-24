package com.email.entity;

import java.sql.Date;
import java.time.LocalDate;
import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Document(value = "customer")
public class Customer {
	
	
	@Id
	private String id;
	private String customerName;
	private String email;
	private String phone;
	private String address;
	
	private Float balance;  // will display how much balance is available to send the email's
	private Boolean isActive;
	private LocalDate createdOn;
	
	
	 
}
