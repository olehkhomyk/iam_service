package com.post_hub.iam_service.service.impl;

import com.post_hub.iam_service.model.dto.kafka.UserEvent;
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

	private final KafkaTemplate<String, UserEvent> kafkaTemplate;

	@Override
	public void publish(UserEvent event) {
		String key = event.getEmail();
		kafkaTemplate.send(TOPIC, key, event);
		log.info("Published event {} for user {}", event.getEventType(), key);
	}
}
