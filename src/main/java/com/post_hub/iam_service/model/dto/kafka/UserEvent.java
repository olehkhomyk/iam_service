package com.post_hub.iam_service.model.dto.kafka;

import com.post_hub.iam_service.model.enums.EventType;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.Map;

@Data
@Builder
public class UserEvent {
	private EventType eventType;
	private LocalDateTime timestamp;
	private Integer userId;
	private String email;
	private Map<String, Object> details;
}