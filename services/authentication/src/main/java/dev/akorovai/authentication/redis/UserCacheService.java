package dev.akorovai.authentication.redis;

import dev.akorovai.authentication.user.User;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class UserCacheService {
	private final RedisTemplate<String, User> redisTemplate;


	public void cacheUser(User user) {
		redisTemplate.opsForValue().set(user.getUsername(), user, 1, TimeUnit.DAYS);
	}

	public User getUser(String username) {
		return redisTemplate.opsForValue().get(username);
	}

	public void evictUser(String nickname) {
		redisTemplate.delete(nickname);
	}
}
