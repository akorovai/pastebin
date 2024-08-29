package dev.akorovai.hashgenerator.event;

import dev.akorovai.hashgenerator.post.PostRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class PostEventListener {

	private final PostRepository repository;

	@EventListener
	@Transactional
	public void onPostViewed(PostViewedEvent event) {
		String hash = event.hash();
		log.info("Handling post viewed event for hash: {}", hash);

		repository.findByHash(hash).ifPresent(post -> {
			post.setViewCount(post.getViewCount() + 1);
			repository.save(post);
			log.info("Updated view count for post with hash: {}", hash);
		});
	}

	@Scheduled(cron = "0 0 0 * * MON")
	@Transactional
	public void halveViewCountsWeekly() {
		log.info("Scheduled task started: halving view counts for all posts");

		var posts = repository.findAll();
		posts.forEach(post -> {
			post.setViewCount(post.getViewCount() / 2);
			log.debug("Halved view count for post with ID: {}", post.hashCode());
		});

		repository.saveAll(posts);

		log.info("Scheduled task completed: halved view counts for {} posts", posts.size());
	}
}
