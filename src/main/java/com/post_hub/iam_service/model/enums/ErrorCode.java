package com.post_hub.iam_service.model.enums;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public enum ErrorCode {
	NOT_FOUND("NOT_FOUND"),
	DATA_ALREADY_EXISTS("DATA_ALREADY_EXISTS"),
	INVALID_PASSWORD("INVALID_PASSWORD"),
	VALIDATION_ERROR("VALIDATION_ERROR"),
	ACCESS_DENIED("ACCESS_DENIED"),
	INTERNAL_SERVER_ERROR("INTERNAL_SERVER_ERROR");

	private final String code;
}
