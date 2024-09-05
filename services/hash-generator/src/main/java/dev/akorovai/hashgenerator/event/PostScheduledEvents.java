package dev.akorovai.hashgenerator.event;

import dev.akorovai.hashgenerator.kafka.KafkaProducer;
import dev.akorovai.hashgenerator.post.Post;
import dev.akorovai.hashgenerator.post.PostRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class PostScheduledEvents {

	private final PostRepository repository;
	private final KafkaProducer producer;

	@Scheduled(fixedRate = 1000)
	@Transactional
	public void deactivateExpiredPosts() {
		try {
			List<Post> expiredPosts = repository.findByExpiresDateBeforeAndActiveTrue(LocalDateTime.now());

			expiredPosts.forEach(post -> {
				post.setActive(false);
				repository.save(post);
				log.info("Deactivated post with hash: {}", post.getHash());
			});
		} catch (Exception e) {
			log.error("Error deactivating expired posts", e);
		}
	}

	@Scheduled(fixedRate = 60000)
	public void cleanupInactivePosts() {
		try {
			LocalDateTime oneWeekAgo = LocalDateTime.now().minusWeeks(1);
			List<String> inactivePostHashes = repository.findInactivePostHashesOlderThanWeek(oneWeekAgo);

			inactivePostHashes.forEach(hash -> {
				producer.sendHash(hash);
				log.info("Sent hash to Kafka: {}", hash);
			});

			repository.deleteInactivePostsOlderThanWeek(oneWeekAgo);
			log.info("Deleted inactive posts older than one week");
		} catch (Exception e) {
			log.error("Error cleaning up inactive posts", e);
		}
	}
}