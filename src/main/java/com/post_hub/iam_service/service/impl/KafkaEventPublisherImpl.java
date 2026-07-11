package com.post_hub.iam_service.service.impl;

import com.post_hub.iam_service.model.dto.kafka.ActionEvent;
import com.post_hub.iam_service.service.KafkaEventPublisher;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class KafkaEventPublisherImpl implements KafkaEventPublisher {
	private static final String TOPIC = "user-events";
	private static final String SERVICE_NAME = "iam-service";

	private final KafkaTemplate<String, ActionEvent> kafkaTemplate;

	@Override
	public void publish(ActionEvent event) {
		event.setService(SERVICE_NAME);
		String key = event.getEmail();
		kafkaTemplate.send(TOPIC, key, event);
		log.info("Published event {} for user {}", event.getEventType(), key);
	}
}
