package dev.akorovai.authentication.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import dev.akorovai.authentication.redis.UserCache;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;
@Slf4j
@Configuration
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class RedisConfig {

	@Value("${spring.redis.host}")
	private String redisHost;

	@Value("${spring.redis.port}")
	private int redisPort;

	@Bean
	public LettuceConnectionFactory redisConnectionFactory() {
		LettuceConnectionFactory factory = new LettuceConnectionFactory(redisHost, redisPort);
		log.info("LettuceConnectionFactory created with hostname: {} and port: {}", redisHost, redisPort);
		return factory;
	}

	@Bean
	public RedisTemplate<String, UserCache> redisTemplate( LettuceConnectionFactory connectionFactory) {
		RedisTemplate<String, UserCache> template = new RedisTemplate<>();
		template.setConnectionFactory(connectionFactory);

		ObjectMapper objectMapper = new ObjectMapper();
		objectMapper.registerModule(new JavaTimeModule());

		template.setKeySerializer(new StringRedisSerializer());
		template.setValueSerializer(new Jackson2JsonRedisSerializer<>(objectMapper, UserCache.class));
		return template;
	}
}