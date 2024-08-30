package dev.akorovai.hashgenerator.event;

import dev.akorovai.hashgenerator.post.Post;
import dev.akorovai.hashgenerator.post.PostRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class PostScheduledEvents {

	private final PostRepository repository;

	@Scheduled(fixedRate = 60000)
	public void deactivateExpiredPosts() {

		List<Post> expiredTextBlocks =
				repository.findByExpiresDateBeforeAndActiveTrue(LocalDateTime.now());


		expiredTextBlocks.forEach(posts -> {
			posts.setActive(false);
			repository.save(posts);
		});
	}


	@Scheduled(cron = "0 0 0 * * MON")
	public void cleanupInactivePosts() {
		LocalDateTime oneWeekAgo = LocalDateTime.now().minusWeeks(1);
		repository.deleteInactivePostsOlderThanWeek(oneWeekAgo);
	}
}
