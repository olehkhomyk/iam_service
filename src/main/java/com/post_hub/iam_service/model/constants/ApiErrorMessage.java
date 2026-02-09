package com.post_hub.iam_service.model.constants;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public enum ApiErrorMessage {
	POST_NOT_FOUND_BY_ID("Post wit ID: %s was not found"),
	POST_ALREADY_EXISTS("Post wit title: %s already exists"),
	USER_NOT_FOUND_BY_ID("User wit ID: %s was not found"),
	USERNAME_ALREADY_EXISTS("User with username: %s already exists"),
	USER_EMAIL_ALREADY_EXISTS("User with email: %s already exists"),
	ROLE_NOT_FOUND("Role with name: %s was not found"),
	EMAIL_NOT_FOUND("Email: %s was not found"),

	INVALID_TOKEN_SIGNATURE("Invalid token signature"),

	ERROR_DURING_JWT_PROCESSING("An unexpected error occurred during JWT processing"),
	TOKEN_EXPIRED("Token expired."),
	UNEXPECTED_ERROR_OCCURRED("An unexpected error occurred. Please try again"),

	AUTHENTICATION_FAILED_FOR_USER("Authentication failed for user: {}"),
	INVALID_USER_OR_PASSWORD("Invalid user or password. Try again"),
	INVALID_USER_REGISTRATION_STATUS("Invalid user registration status: %s"),
	NOT_FOUND_REFRESH_TOKEN("Refresh token not found"),

	;

	private final String message;

	public String getMessage(Object... args) {
		return String.format(message, args);
	}
}
