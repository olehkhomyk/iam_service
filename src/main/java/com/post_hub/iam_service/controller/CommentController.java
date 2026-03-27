package com.post_hub.iam_service.controller;

import com.post_hub.iam_service.model.constants.ApiLogMessage;
import com.post_hub.iam_service.model.dto.comment.CommentDTO;
import com.post_hub.iam_service.model.dto.post.PostDTO;
import com.post_hub.iam_service.model.request.comment.CommentRequest;
import com.post_hub.iam_service.model.request.post.UpdatePostRequest;
import com.post_hub.iam_service.model.respsonse.IamResponse;
import com.post_hub.iam_service.model.respsonse.PaginationResponse;
import com.post_hub.iam_service.service.CommentService;
import com.post_hub.iam_service.utils.ApiUtils;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@Slf4j
@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("${end.point.comments}")
@Tag(name = "Comments", description = "Comment endpoints")
public class CommentController {
	private final CommentService commentService;
	private final ApiUtils apiUtils;

	@PostMapping
	public ResponseEntity<IamResponse<CommentDTO>> create(
			@PathVariable(name = "postId") Integer postId,
			@Valid @RequestBody CommentRequest request,
			Principal principal) {
		log.trace(ApiLogMessage.NAME_OF_CURRENT_METHOD.getValue(), ApiUtils.getMethodName());

		IamResponse<CommentDTO> result = commentService.create(postId, request, principal.getName());
		return ResponseEntity.ok(result);
	}

	@GetMapping
	public ResponseEntity<IamResponse<PaginationResponse<CommentDTO>>> getByPostId(
			@PathVariable(name = "postId") Integer postId,
			@RequestParam(name = "page", defaultValue = "0") int page,
			@RequestParam(name = "limit", defaultValue = "10") int limit
	) {
		log.trace(ApiLogMessage.NAME_OF_CURRENT_METHOD.getValue(), ApiUtils.getMethodName());
		Pageable pageable = PageRequest.of(page, limit);

		IamResponse<PaginationResponse<CommentDTO>> result = commentService.getAllByPostId(postId, pageable);
		return ResponseEntity.ok(result);
	}

	@GetMapping("/{id}")
	public ResponseEntity<IamResponse<CommentDTO>> getById(
			@PathVariable(name = "postId") Integer postId,
			@PathVariable(name = "id") Integer id) {
		log.trace(ApiLogMessage.NAME_OF_CURRENT_METHOD.getValue(), ApiUtils.getMethodName());

		IamResponse<CommentDTO> result = commentService.getById(postId, id);
		return ResponseEntity.ok(result);
	}

	@DeleteMapping("${end.point.id}")
	public ResponseEntity<Void> deleteById(
			@PathVariable(name = "postId") Integer postId,
			@PathVariable(name = "id") Integer id) {
		log.trace(ApiLogMessage.NAME_OF_CURRENT_METHOD.getValue(), ApiUtils.getMethodName());

		commentService.deleteById(postId, id);

		return ResponseEntity.ok().build();
	}

	@PostMapping("${end.point.id}/like")
	public ResponseEntity<Void> likeComment(@PathVariable(name = "id") Integer commentId) {
		log.trace(ApiLogMessage.NAME_OF_CURRENT_METHOD.getValue(), ApiUtils.getMethodName());
		Integer userId = apiUtils.getUserIdFromAuthentication();

		commentService.likeComment(commentId, userId);
		return ResponseEntity.ok().build();
	}

	@DeleteMapping("${end.point.id}/like")
	public ResponseEntity<Void> unlikeComment(@PathVariable(name = "id") Integer commentId) {
		log.trace(ApiLogMessage.NAME_OF_CURRENT_METHOD.getValue(), ApiUtils.getMethodName());
		Integer userId = apiUtils.getUserIdFromAuthentication();

		commentService.unlikeComment(commentId, userId);
		return ResponseEntity.ok().build();
	}
}
