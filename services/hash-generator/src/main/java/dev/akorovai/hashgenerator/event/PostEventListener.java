package dev.akorovai.hashgenerator.event;

import dev.akorovai.hashgenerator.post.PostRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PostEventListener {
	private final PostRepository repository;

	@EventListener
	@Transactional
	public void onPostViewed(PostViewedEvent event) {
		String hash = event.hash();

		repository.findByHash(hash).ifPresent(post -> {
			post.setViewCount(post.getViewCount() + 1);
			repository.save(post);
		});
	}


	@Scheduled(cron = "0 0 0 * * MON")
	@Transactional
	public void halveViewCountsWeekly(){
		var posts = repository.findAll();
		posts.forEach(post -> post.setViewCount(post.getViewCount() + 1));
		repository.saveAll(posts);
	}

}
