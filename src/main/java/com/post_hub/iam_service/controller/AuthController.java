package com.post_hub.iam_service.controller;

import com.post_hub.iam_service.model.constants.ApiLogMessage;
import com.post_hub.iam_service.model.request.user.LoginRequest;
import com.post_hub.iam_service.model.dto.user.UserProfileDTO;
import com.post_hub.iam_service.model.request.user.RegistrationUserRequest;
import com.post_hub.iam_service.model.respsonse.IamResponse;
import com.post_hub.iam_service.service.AuthService;
import com.post_hub.iam_service.utils.ApiUtils;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("${end.point.auth}")
@Tag(name = "Auth", description = "Authorization methods")
public class AuthController {
	private final AuthService authService;

	@PostMapping("${end.point.login}")
	public ResponseEntity<IamResponse<UserProfileDTO>> login(
			@RequestBody @Valid LoginRequest request,
			HttpServletResponse response) {
		log.trace(ApiLogMessage.NAME_OF_CURRENT_METHOD.getValue(), ApiUtils.getMethodName());

		IamResponse<UserProfileDTO> result = authService.login(request);
		Cookie authorizationCookie = ApiUtils.createAuthCookie(result.getPayload().getToken());
		response.addCookie(authorizationCookie);

		return ResponseEntity.ok(result);
	}

	@PostMapping("${end.point.register}")
	public ResponseEntity<IamResponse<UserProfileDTO>> register(
			@RequestBody @Valid RegistrationUserRequest request,
			HttpServletResponse response) {
		log.trace(ApiLogMessage.NAME_OF_CURRENT_METHOD.getValue(), ApiUtils.getMethodName());

		IamResponse<UserProfileDTO> result = authService.registerUser(request);
		Cookie authorizationCookie = ApiUtils.createAuthCookie(result.getPayload().getToken());
		response.addCookie(authorizationCookie);

		return ResponseEntity.ok(result);
	}

	@GetMapping("${end.point.refresh.token}")
	public ResponseEntity<IamResponse<UserProfileDTO>> refreshToken(
			@RequestParam(name = "token") String refreshToken,
			HttpServletResponse response
	) {
		log.trace(ApiLogMessage.NAME_OF_CURRENT_METHOD.getValue(), ApiUtils.getMethodName());

		IamResponse<UserProfileDTO> result = authService.refreshAccessToken(refreshToken);
		Cookie authorizationCookie = ApiUtils.createAuthCookie(result.getPayload().getToken());
		response.addCookie(authorizationCookie);

		return ResponseEntity.ok(result);
	}
}
