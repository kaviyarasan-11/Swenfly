package com.email.dto;

import java.time.LocalDateTime;

import org.springframework.data.annotation.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserEmailDto {
	
	@Id
	private String id;
	private String userName;
	private LocalDateTime runTime;
	private String fileName;
	private byte[] attachment;

}
