package com.post_hub.iam_service.controller;

import com.post_hub.iam_service.model.constants.ApiLogMessage;
import com.post_hub.iam_service.model.dto.file.FileDTO;
import com.post_hub.iam_service.model.respsonse.IamResponse;
import com.post_hub.iam_service.service.FileService;
import com.post_hub.iam_service.utils.ApiUtils;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("${end.point.files}")
@Tag(name = "Files", description = "Files endpoints")
public class FileController {
	private final FileService fileService;

	@PostMapping("/upload")
	public ResponseEntity<IamResponse<FileDTO>> upload(@RequestParam("file") MultipartFile file) {
		log.trace(ApiLogMessage.NAME_OF_CURRENT_METHOD.getValue(), ApiUtils.getMethodName());
		IamResponse<FileDTO> response = fileService.upload(file);

		return ResponseEntity.ok(response);
	}

	@DeleteMapping
	public ResponseEntity<IamResponse<FileDTO>> delete(@RequestParam("key") String key) {
		log.trace(ApiLogMessage.NAME_OF_CURRENT_METHOD.getValue(), ApiUtils.getMethodName());
		fileService.delete(key);

		return ResponseEntity.ok().build();
	}

	@GetMapping("/{key:.+}")
	public ResponseEntity<Resource> get(@PathVariable("key") String key) {
		byte[] data = fileService.download(key);
		ByteArrayResource resource = new ByteArrayResource(data);

		return ResponseEntity.ok()
				.contentType(MediaType.IMAGE_JPEG)
				.body(resource);
	}
}
