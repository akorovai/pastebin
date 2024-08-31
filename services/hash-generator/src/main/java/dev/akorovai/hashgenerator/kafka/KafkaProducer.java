package dev.akorovai.hashgenerator.kafka;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class KafkaProducer {
	@Value("${kafka.topicName}")
	private String topicName;

	private final KafkaTemplate<String, String> kafkaTemplate;

	public void sendHash(String hash){
		log.info("Sending hash: " + hash);
		kafkaTemplate.send(topicName, hash);
	}
}
