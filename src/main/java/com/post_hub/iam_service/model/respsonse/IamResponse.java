package com.post_hub.iam_service.model.respsonse;

import com.post_hub.iam_service.model.constants.ApiMessage;
import com.post_hub.iam_service.model.enums.ErrorCode;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.apache.commons.lang3.StringUtils;

import java.io.Serializable;
import java.util.List;


@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class IamResponse<P extends Serializable> implements Serializable {
	private String message;
	private P payload;
	private boolean success;

	public static <P extends Serializable> IamResponse<P> createSuccess(P payload) {
		return new IamResponse<P>(StringUtils.EMPTY, payload, true);
	}

	public static <P extends Serializable> IamResponse<P> createSuccessfulWithNewToken(P payload) {
		return new IamResponse<P>(ApiMessage.TOKEN_CREATED_OR_UPDATED.getMessage(), payload, true);
	}

	public static IamResponse<ErrorResponse> createError(ErrorCode errorCode, String message) {
		ErrorResponse errorResponse = new ErrorResponse(errorCode.getCode(), message);
		return new IamResponse<>(message, errorResponse, false);
	}

	public static IamResponse<ErrorResponse> createError(ErrorCode errorCode, String message, List<String> details) {
		ErrorResponse errorResponse = new ErrorResponse(errorCode.getCode(), message, details);
		return new IamResponse<>(message, errorResponse, false);
	}
}
