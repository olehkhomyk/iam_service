package com.post_hub.iam_service.service.impl;

import com.post_hub.iam_service.config.MinioProperties;
import com.post_hub.iam_service.service.MinioService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.DeleteObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

import java.io.IOException;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class MinioServiceImpl implements MinioService {
	private final S3Client s3Client;
	private final MinioProperties properties;

	@Override
	public String upload(MultipartFile file) {
		String key = generateKey(file.getOriginalFilename());

		try {
			s3Client.putObject(
					PutObjectRequest.builder()
							.bucket(properties.getBucket())
							.key(key)
							.contentType(file.getContentType())
							.build(),
					RequestBody.fromBytes(file.getBytes())
			);
		} catch (IOException e) {
			throw new RuntimeException("Failed to upload file", e);
		}

		return key;
	}

	@Override
	public void delete(String key) {
		s3Client.deleteObject(
				DeleteObjectRequest.builder()
						.bucket(properties.getBucket())
						.key(key)
						.build()
		);
	}

	@Override
	public String getUrl(String key) {
		return properties.getUrl()
				+ "/" + properties.getBucket()
				+ "/" + key;
	}

	private String generateKey(String originalFilename) {
		return UUID.randomUUID() + "_" + originalFilename;
	}
}
