package dev.akorovai.authentication.redis;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserCacheService {
	private final RedisTemplate<String, UserCache> redisTemplate;

	public void cacheUser(UserCache user) {
		log.debug("Caching user: {}", user.getNickname());
		redisTemplate.opsForValue().set(user.getNickname(), user, 1, TimeUnit.DAYS);
		log.info("User cached: {}", user.getNickname());
	}

	public UserCache getUser(String username) {
		log.debug("Fetching user from cache: {}", username);
		UserCache user = redisTemplate.opsForValue().get(username);
		if (user != null) {
			log.info("User found in cache: {}", username);
		} else {
			log.debug("User not found in cache: {}", username);
		}
		return user;
	}
}