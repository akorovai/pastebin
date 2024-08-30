package dev.akorovai.hashgenerator.redis;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class RedisService {

	private final RedisTemplate<String, String> redisTemplate;

	public void pushValue(final String key, final String value){
		redisTemplate.opsForList().rightPush(key, value);
	}

	public String  pullValue(final String key){
		return redisTemplate.opsForList().leftPop(key);
	}

	public Long getSize(final String key){
		return redisTemplate.opsForList().size(key);
	}

}
