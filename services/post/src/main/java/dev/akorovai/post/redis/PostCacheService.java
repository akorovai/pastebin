package dev.akorovai.post.redis;

import dev.akorovai.post.post.PostResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.Cursor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ScanOptions;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class PostCacheService {

	private final RedisTemplate<String, PostCache> postsRedisTemplate;

	@Qualifier("customStringRedisTemplate")
	private final RedisTemplate<String, String> postsLeaderBoardsRedisTemplate;
	private static final String LIST_NAME = "POPULAR_POSTS";
	private static final int TOP_LIMIT = 100;

	public void savePost( PostResponse post, boolean isPublic) {
		postsLeaderBoardsRedisTemplate.opsForZSet().add(LIST_NAME,
				post.hash(),
				0);
		postsRedisTemplate.opsForValue().set(post.hash(),PostCache.builder()
				                                            .s3Url(post.s3Url())
				                                            .language(post.language())
				                                            .createdDate(post.createdDate())
				                                            .expiresDate(post.expiresDate())
				                                            .userId(post.userId())
				                                                 .isPublic(isPublic)
				                                            .build());
	}

	public PostCache getPost(String hash) {
		PostCache postCache = postsRedisTemplate.opsForValue().get(hash);
		if (postCache == null) {
			log.warn("Post with hash {} not found in cache", hash);
			return null;
		}
		postsLeaderBoardsRedisTemplate.opsForZSet().incrementScore(LIST_NAME, hash, 1);
		log.info("Post retrieved and counter incremented for hash: {}", hash);
		return postCache;
	}

	public void deletePost(String hash) {
		postsRedisTemplate.delete(hash);
		postsLeaderBoardsRedisTemplate.delete(hash);
		log.info("Post deleted with hash: {}", hash);
	}

	public void resetAllCounters() {
		ScanOptions scanOptions = ScanOptions.scanOptions().match("*").build();
		try (Cursor<ZSetOperations.TypedTuple<String>> cursor = postsLeaderBoardsRedisTemplate.opsForZSet().scan(LIST_NAME, scanOptions)) {
			while (cursor.hasNext()) {
				ZSetOperations.TypedTuple<String> tuple = cursor.next();
				String hash = Objects.requireNonNull(tuple.getValue());
				postsLeaderBoardsRedisTemplate.opsForZSet().add(LIST_NAME, hash, 0);
			}
			log.info("All counters reset successfully");
		}
	}

	public Set<RatingObject> getMostPopularPosts() {
		Set<ZSetOperations.TypedTuple<String>> topPosts =
				postsLeaderBoardsRedisTemplate.opsForZSet().reverseRangeWithScores(LIST_NAME,
						0, TOP_LIMIT - 1);

		if (topPosts == null || topPosts.isEmpty()) {
			log.info("No popular posts found");
			return Collections.emptySet();
		}

		Set<RatingObject> popularPosts = topPosts.stream()
				                                 .map(tuple -> RatingObject.builder()
						                                               .number(String.valueOf(tuple.getScore()))
						                                               .hash(Objects.requireNonNull(tuple.getValue()))
						                                               .build())
				                                 .collect(Collectors.toSet());

		log.info("Retrieved {} most popular posts", popularPosts.size());
		return popularPosts;
	}



}
