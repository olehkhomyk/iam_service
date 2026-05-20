package com.post_hub.iam_service.service;

import org.springframework.web.multipart.MultipartFile;

public interface MinioService {
	String upload(MultipartFile file);

	void delete(String key);

	String getUrl(String key);

	byte[] download(String key);
}
