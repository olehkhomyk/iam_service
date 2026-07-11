package com.post_hub.iam_service.model.constants;

import lombok.AllArgsConstructor;
import lombok.Getter;

import static lombok.AccessLevel.PRIVATE;

@Getter
@AllArgsConstructor(access = PRIVATE)
public enum ApiKafkaMessage {

	USER_LOGGED_IN("User '%s' logged in"),
	USER_REGISTERED("User '%s' registered");

	private final String template;

	public String format(Object... args) {
		return String.format(template, args);
	}
}
