package com.post_hub.iam_service.service;

import com.post_hub.iam_service.model.dto.file.FileDTO;
import com.post_hub.iam_service.model.respsonse.IamResponse;
import org.springframework.web.multipart.MultipartFile;

public interface FileService {
	IamResponse<FileDTO> upload(MultipartFile file);

	byte[] download(String key);

	void delete(String key);
}
