package com.post_hub.iam_service.controller;

import com.post_hub.iam_service.model.constants.ApiLogMessage;
import com.post_hub.iam_service.model.dto.user.UserDTO;
import com.post_hub.iam_service.model.dto.user.UserSearchDTO;
import com.post_hub.iam_service.model.request.user.NewUserRequest;
import com.post_hub.iam_service.model.request.user.UpdateUserRequest;
import com.post_hub.iam_service.model.request.user.UserSearchRequest;
import com.post_hub.iam_service.model.respsonse.IamResponse;
import com.post_hub.iam_service.model.respsonse.PaginationResponse;
import com.post_hub.iam_service.service.UserService;
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

@Slf4j
@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("${end.point.users}")
@Tag(name = "Users", description = "User endpoints")
public class UserController {
	private final UserService userService;

	@GetMapping("${end.point.id}")
	public ResponseEntity<IamResponse<UserDTO>> getUserById(@PathVariable(name = "id") Integer userI) {
		log.trace(ApiLogMessage.NAME_OF_CURRENT_METHOD.getValue(), ApiUtils.getMethodName());

		IamResponse<UserDTO> response = userService.getById(userI);

		return ResponseEntity.ok(response);
	}

	@PostMapping("${end.point.create}")
	public ResponseEntity<IamResponse<UserDTO>> createUser(@Valid @RequestBody NewUserRequest request) {
		log.trace(ApiLogMessage.NAME_OF_CURRENT_METHOD.getValue(), ApiUtils.getMethodName());

		IamResponse<UserDTO> response = userService.createUser(request);

		return ResponseEntity.ok(response);
	}

	@PutMapping("${end.point.id}")
	public ResponseEntity<IamResponse<UserDTO>> updateUser(
			@PathVariable(name = "id") Integer id,
			@Valid @RequestBody UpdateUserRequest request) {
		log.trace(ApiLogMessage.NAME_OF_CURRENT_METHOD.getValue(), ApiUtils.getMethodName());

		IamResponse<UserDTO> updatedUser = userService.updateUserById(id, request);

		return ResponseEntity.ok(updatedUser);
	}

	@DeleteMapping("${end.point.id}")
	public ResponseEntity<Void> softDeleteUserById(@PathVariable(name = "id") Integer id) {
		log.trace(ApiLogMessage.NAME_OF_CURRENT_METHOD.getValue(), ApiUtils.getMethodName());

		userService.softDeleteUserById(id);

		return ResponseEntity.ok().build();
	}

	@DeleteMapping("${end.point.id}/hard")
	public ResponseEntity<Void> hardDeleteUserById(@PathVariable(name = "id") Integer id) {
		log.trace(ApiLogMessage.NAME_OF_CURRENT_METHOD.getValue(), ApiUtils.getMethodName());

		userService.hardDeleteUserById(id);

		return ResponseEntity.ok().build();
	}

	@PostMapping("${end.point.search}")
	public ResponseEntity<IamResponse<PaginationResponse<UserSearchDTO>>> searchUsers(
			@RequestBody @Valid UserSearchRequest request,
			@RequestParam(name = "page", defaultValue = "0") int page,
			@RequestParam(name = "limit", defaultValue = "10") int limit) {
		log.trace(ApiLogMessage.NAME_OF_CURRENT_METHOD.getValue(), ApiUtils.getMethodName());

		Pageable pageable = PageRequest.of(page, limit);
		IamResponse<PaginationResponse<UserSearchDTO>> response = userService.searchUsers(request, pageable);

		return ResponseEntity.ok(response);
	}

	@GetMapping("${end.point.all}")
	public ResponseEntity<IamResponse<PaginationResponse<UserSearchDTO>>> getAllUsers(
			@RequestParam(name = "page", defaultValue = "0") int page,
			@RequestParam(name = "limit", defaultValue = "10") int limit) {
		log.trace(ApiLogMessage.NAME_OF_CURRENT_METHOD.getValue(), ApiUtils.getMethodName());

		Pageable pageable = PageRequest.of(page, limit);
		IamResponse<PaginationResponse<UserSearchDTO>> response = userService.findAllUsers(pageable);

		return ResponseEntity.ok(response);
	}
}
