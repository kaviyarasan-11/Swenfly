package com.framework;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
@Configuration
public class AmazonS3Config {
@Value("${amazon.s3.region}")
private String region;
@Value("${amazon.s3.accessKey}")
private String accessKey;
@Value("${amazon.s3.secretKey}")
private String secretKey;
@Bean
public AmazonS3 amazonS3() {
AWSCredentials credentials = new BasicAWSCredentials(accessKey, secretKey);
return AmazonS3ClientBuilder.standard()
.withCredentials(new AWSStaticCredentialsProvider(credentials))
.withRegion((region))
.build();
}
}
