package com.email.dto;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Document(value = "shareEmailRequest")
public class ShareEmailRequest {
	    @Id
	    private String id;
	    private String toAddress;
	    private String longUrl;
	    private String shortUrl;
}
