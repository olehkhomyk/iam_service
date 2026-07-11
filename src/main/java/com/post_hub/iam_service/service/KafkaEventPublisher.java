package com.post_hub.iam_service.service;

import com.post_hub.iam_service.model.dto.kafka.ActionEvent;

public interface KafkaEventPublisher {
	void publish(ActionEvent event);
}
