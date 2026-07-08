package com.post_hub.iam_service.service;

import com.post_hub.iam_service.model.dto.kafka.UserEvent;

public interface KafkaEventPublisher {
	void publish(UserEvent event);
}
