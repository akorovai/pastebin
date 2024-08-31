package dev.akorovai.post.kafka;

import dev.akorovai.post.aws.StorageService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class KafkaConsumer {
	private final StorageService storageService;
	@KafkaListener(topics = "delete-posts-topic", groupId = "postGroup")
	public void consumeHash(String hash){
		log.info("Consumed hash: {}", hash);
		storageService.delete(hash);
		log.info("Deleted post with hash: {}", hash);
	}
}
