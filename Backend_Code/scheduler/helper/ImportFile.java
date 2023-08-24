package com.scheduler.helper;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.security.DigestInputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.amazonaws.services.s3.AmazonS3;

import com.amazonaws.services.s3.model.S3Object;

@Service
public class ImportFile {

	private static final Logger LOGGER = LoggerFactory.getLogger(ImportFile.class);

	@Autowired
	private AmazonS3 s3Client;

	public void download(String fileName) throws IOException {
		S3Object s3Object = s3Client.getObject("emailfiles55/Framework/", fileName);

		Path destination = new File("D:\\" + fileName).toPath();
		try {
			Files.copy(s3Object.getObjectContent(), destination, StandardCopyOption.REPLACE_EXISTING);

			// Validate checksum
			String expectedChecksum = s3Object.getObjectMetadata().getETag().replace("\"", "");
			if (isChecksumValid(destination.toFile(), expectedChecksum)) {
				LOGGER.info("File is valid: {}", fileName);
				System.out.println("File is valid: " + fileName);
			} else {
				LOGGER.error("File checksum is invalid: {}", fileName);
				System.out.println("File checksum is invalid: " + fileName);
			}
		} finally {
			s3Object.close();
		}
	}

	private boolean isChecksumValid(File file, String expectedChecksum) throws IOException {
		try (FileInputStream fis = new FileInputStream(file)) {
			MessageDigest md = MessageDigest.getInstance("MD5");
			DigestInputStream dis = new DigestInputStream(fis, md);
			byte[] buffer = new byte[8192];
			while (dis.read(buffer) != -1) {
				// Read the file while updating the checksum
			}
			String actualChecksum = bytesToHex(md.digest());
			return actualChecksum.equalsIgnoreCase(expectedChecksum);
		} catch (NoSuchAlgorithmException e) {
			throw new RuntimeException("MD5 algorithm not available", e);
		}
	}

	private String bytesToHex(byte[] bytes) {
		StringBuilder sb = new StringBuilder();
		for (byte b : bytes) {
			sb.append(String.format("%02x", b));
		}
		return sb.toString();
	}
}
