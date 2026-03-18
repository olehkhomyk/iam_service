package com.post_hub.iam_service.controller;

import com.post_hub.iam_service.model.constants.ApiLogMessage;
import com.post_hub.iam_service.model.dto.post.PostDTO;
import com.post_hub.iam_service.model.dto.post.PostSearchDTO;
import com.post_hub.iam_service.model.request.post.PostRequest;
import com.post_hub.iam_service.model.request.post.PostSearchRequest;
import com.post_hub.iam_service.model.request.post.UpdatePostRequest;
import com.post_hub.iam_service.model.respsonse.IamResponse;
import com.post_hub.iam_service.model.respsonse.PaginationResponse;
import com.post_hub.iam_service.service.PostService;
import com.post_hub.iam_service.utils.ApiUtils;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.data.domain.Pageable;

import java.security.Principal;


@Slf4j
@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("${end.point.posts}")
@Tag(name = "Posts", description = "Post endpoints")
public class PostController {
	private final PostService postService;

	@GetMapping("${end.point.id}")
	public ResponseEntity<IamResponse<PostDTO>> getPostById(
			@PathVariable(name = "id") Integer id) {
		log.trace(ApiLogMessage.NAME_OF_CURRENT_METHOD.getValue(), ApiUtils.getMethodName());

		IamResponse<PostDTO> response = postService.getById(id);
		return ResponseEntity.ok(response);
	}

	@GetMapping("${end.point.all}")
	public ResponseEntity<IamResponse<PaginationResponse<PostSearchDTO>>> getAllPosts(
			@RequestParam(name = "page", defaultValue = "0") int page,
			@RequestParam(name = "limit", defaultValue = "10") int limit,
			@RequestParam(name = "includeComments", required = false, defaultValue = "false") Boolean includeComments) {
		log.trace(ApiLogMessage.NAME_OF_CURRENT_METHOD.getValue(), ApiUtils.getMethodName());

		Pageable pageable = PageRequest.of(page, limit);
		IamResponse<PaginationResponse<PostSearchDTO>> response = postService.findAllPosts(pageable, includeComments);

		return ResponseEntity.ok(response);
	}

	@PostMapping("${end.point.search}")
	public ResponseEntity<IamResponse<PaginationResponse<PostSearchDTO>>> searchPosts(
			@RequestBody @Valid PostSearchRequest request,
			@RequestParam(name = "page", defaultValue = "0") int page,
			@RequestParam(name = "limit", defaultValue = "10") int limit,
			@RequestParam(name = "includeComments", required = false, defaultValue = "false") Boolean includeComments) {
		log.trace(ApiLogMessage.NAME_OF_CURRENT_METHOD.getValue(), ApiUtils.getMethodName());

		Pageable pageable = PageRequest.of(page, limit);
		IamResponse<PaginationResponse<PostSearchDTO>> response = postService.searchPosts(request, pageable, includeComments);
		return ResponseEntity.ok(response);
	}

	@PostMapping("${end.point.create}")
	public ResponseEntity<IamResponse<PostDTO>> createPost(
			@RequestBody @Valid PostRequest postRequest,
			Principal principal) {
		log.trace(ApiLogMessage.NAME_OF_CURRENT_METHOD.getValue(), ApiUtils.getMethodName());

		IamResponse<PostDTO> response = postService.create(postRequest, principal.getName());
		return ResponseEntity.ok(response);
	}

	@PutMapping("${end.point.id}")
	public ResponseEntity<IamResponse<PostDTO>> updatePostById(
			@PathVariable(name = "id") Integer id,
			@RequestBody @Valid UpdatePostRequest request) {
		log.trace(ApiLogMessage.NAME_OF_CURRENT_METHOD.getValue(), ApiUtils.getMethodName());

		IamResponse<PostDTO> updatedPost = postService.update(id, request);
		return ResponseEntity.ok(updatedPost);
	}

	@DeleteMapping("${end.point.id}")
	public ResponseEntity<Void> softDeletePostById(
			@PathVariable(name = "id") Integer id) {
		log.trace(ApiLogMessage.NAME_OF_CURRENT_METHOD.getValue(), ApiUtils.getMethodName());

		postService.sofDeletePost(id);

		return ResponseEntity.ok().build();
	}

	@DeleteMapping("${end.point.id}/hard")
	public ResponseEntity<Void> hardDeletePostById(
			@PathVariable(name = "id") Integer id) {
		log.trace(ApiLogMessage.NAME_OF_CURRENT_METHOD.getValue(), ApiUtils.getMethodName());

		postService.hardDeletePost(id);

		return ResponseEntity.ok().build();
	}
}
