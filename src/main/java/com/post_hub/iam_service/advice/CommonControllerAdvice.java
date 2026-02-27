package com.post_hub.iam_service.advice;

import com.post_hub.iam_service.model.constants.ApiConstants;
import com.post_hub.iam_service.model.enums.ErrorCode;
import com.post_hub.iam_service.model.exception.DataExistException;
import com.post_hub.iam_service.model.exception.InvalidPasswordException;
import com.post_hub.iam_service.model.exception.NotFoundException;
import com.post_hub.iam_service.model.respsonse.ErrorResponse;
import com.post_hub.iam_service.model.respsonse.IamResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Slf4j
@ControllerAdvice
public class CommonControllerAdvice {

	@ExceptionHandler
	@ResponseBody
	protected ResponseEntity<IamResponse<ErrorResponse>> handleNotFoundException(NotFoundException ex) {
		logStackTrace(ex);

		IamResponse<ErrorResponse> response = IamResponse.createError(
				ErrorCode.NOT_FOUND,
				ex.getMessage()
		);

		return ResponseEntity
				.status(HttpStatus.NOT_FOUND)
				.body(response);
	}

	@ExceptionHandler(DataExistException.class)
	@ResponseBody
	protected ResponseEntity<IamResponse<ErrorResponse>> handleDataExistException(DataExistException ex) {
		logStackTrace(ex);

		IamResponse<ErrorResponse> response = IamResponse.createError(
				ErrorCode.DATA_ALREADY_EXISTS,
				ex.getMessage()
		);

		return ResponseEntity
				.status(HttpStatus.CONFLICT)
				.body(response);
	}

	@ExceptionHandler(InvalidPasswordException.class)
	@ResponseBody
	public ResponseEntity<IamResponse<ErrorResponse>> handleInvalidPasswordException(InvalidPasswordException ex) {
		IamResponse<ErrorResponse> response = IamResponse.createError(
				ErrorCode.INVALID_PASSWORD,
				ex.getMessage()
		);

		return ResponseEntity
				.status(HttpStatus.BAD_REQUEST)
				.body(response);
	}

	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<IamResponse<ErrorResponse>> handleMethodArgumentNotValidException(MethodArgumentNotValidException ex) {
		logStackTrace(ex);

		List<String> validationErrors = ex.getBindingResult().getAllErrors().stream()
				.map(error -> {
					if (error instanceof FieldError fieldError) {
						return fieldError.getField() + ": " + error.getDefaultMessage();
					}
					return error.getDefaultMessage();
				})
				.collect(Collectors.toList());

		IamResponse<ErrorResponse> response = IamResponse.createError(
				ErrorCode.VALIDATION_ERROR,
				"Validation failed",
				validationErrors
		);

		return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler(AccessDeniedException.class)
	@ResponseBody
	protected ResponseEntity<IamResponse<ErrorResponse>> handleAccessDeniedException(AccessDeniedException ex) {
		logStackTrace(ex);

		IamResponse<ErrorResponse> response = IamResponse.createError(
				ErrorCode.ACCESS_DENIED,
				ex.getMessage()
		);

		return ResponseEntity
				.status(HttpStatus.FORBIDDEN)
				.body(response);
	}

	private void logStackTrace(Exception ex) {
		StringBuilder stackTrace = new StringBuilder();

		stackTrace.append(ApiConstants.ANSI_RED);

		stackTrace.append(ex.getMessage()).append(ApiConstants.BREAK_LINE);

		if (Objects.nonNull(ex.getCause())) {
			stackTrace.append(ex.getCause().getMessage()).append(ApiConstants.BREAK_LINE);
		}

		Arrays.stream(ex.getStackTrace())
				.filter(st -> st.getClassName().startsWith(ApiConstants.TIME_ZONE_PACKAGE_NAME))
				.forEach(st -> stackTrace
						.append(st.getClassName())
						.append(".")
						.append(st.getMethodName())
						.append(" (")
						.append(st.getLineNumber())
						.append(") ")
				);

		log.error(stackTrace.append(ApiConstants.ANSI_WHITE).toString());
	}
}
