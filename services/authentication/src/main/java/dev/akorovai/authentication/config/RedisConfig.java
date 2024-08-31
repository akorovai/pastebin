package dev.akorovai.authentication.config;

import dev.akorovai.authentication.user.User;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisClusterConfiguration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

@Configuration
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class RedisConfig {

	private final RedisClusterProperties redisClusterProperties;


	@Bean
	public RedisConnectionFactory redisConnectionFactory() {
		RedisClusterConfiguration clusterConfiguration = new RedisClusterConfiguration(redisClusterProperties.getNodes());
		return new LettuceConnectionFactory(clusterConfiguration);
	}

	@Bean
	public RedisTemplate<String, User> redisTemplate(RedisConnectionFactory redisConnectionFactory) {
		RedisTemplate<String, User> template = new RedisTemplate<>();
		template.setConnectionFactory(redisConnectionFactory);
		template.setKeySerializer(new StringRedisSerializer());
		template.setValueSerializer(new Jackson2JsonRedisSerializer<>(User.class));
		return template;
	}
}

