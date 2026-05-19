package com.post_hub.iam_service.service.impl;

import com.post_hub.iam_service.model.constants.ApiErrorMessage;
import com.post_hub.iam_service.model.constants.FileConstants;
import com.post_hub.iam_service.model.dto.file.FileDTO;
import com.post_hub.iam_service.model.exception.InvalidDataException;
import com.post_hub.iam_service.model.respsonse.IamResponse;
import com.post_hub.iam_service.service.FileService;
import com.post_hub.iam_service.service.MinioService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
public class FileServiceImpl implements FileService {
	private final MinioService minioService;

	@Override
	public IamResponse<FileDTO> upload(MultipartFile file) {
		if (file.isEmpty()) {
			throw new InvalidDataException(ApiErrorMessage.EMPTY_FILE.getMessage());
		}

		if (file.getSize() > FileConstants.MAX_UPLOAD_SIZE_MB * 1024 * 1024) {
			throw new InvalidDataException(ApiErrorMessage.FILE_SIZE_EXCEEDED.getMessage(FileConstants.MAX_UPLOAD_SIZE_MB));
		}

		String key = minioService.upload(file);
		String url = minioService.getUrl(key);

		FileDTO  fileDTO = new FileDTO(key, url);

		return IamResponse.createSuccess(fileDTO);
	}

	@Override
	public void delete(String key) {
		minioService.delete(key);
	}
}
