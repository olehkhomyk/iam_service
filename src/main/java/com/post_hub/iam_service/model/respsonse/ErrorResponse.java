package com.post_hub.iam_service.model.respsonse;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ErrorResponse implements Serializable {
	private String errorCode;
	private String message;
	private LocalDateTime timestamp;
	private List<String> details;

	public ErrorResponse(String errorCode, String message) {
		this.errorCode = errorCode;
		this.message = message;
		this.timestamp = LocalDateTime.now();
		this.details = null;
	}

	public ErrorResponse(String errorCode, String message, List<String> details) {
		this.errorCode = errorCode;
		this.message = message;
		this.timestamp = LocalDateTime.now();
		this.details = details;
	}
}
