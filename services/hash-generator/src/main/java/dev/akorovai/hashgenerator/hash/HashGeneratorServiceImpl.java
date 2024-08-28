package dev.akorovai.hashgenerator.hash;

import dev.akorovai.hashgenerator.excepion.HashGenerationException;
import dev.akorovai.hashgenerator.post.PostRepository;
import jakarta.annotation.PostConstruct;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.UUID;

@Getter
enum HashConfig {
	HASH_KEY("available_hashes"),
	HASH_POOL_SIZE(10);

	private final String stringValue;
	private final int intValue;

	HashConfig(String stringValue) {
		this.stringValue = stringValue;
		this.intValue = 0;
	}

	HashConfig(int intValue) {
		this.stringValue = null;
		this.intValue = intValue;
	}
}

@Service
@RequiredArgsConstructor(onConstructor_ = {@Autowired})
@Slf4j
public class HashGeneratorServiceImpl implements HashGeneratorService {

	private final RedisTemplate<String, String> redisTemplate;
	private final PostRepository repository;

	@PostConstruct
	public void preloadHashes() {
		Long size = redisTemplate.opsForList().size(HashConfig.HASH_KEY.getStringValue());
		if (size == null || size < HashConfig.HASH_POOL_SIZE.getIntValue()) {
			log.info("Preloading {} hashes into Redis...", HashConfig.HASH_POOL_SIZE.getIntValue() - (size == null ? 0 : size));
			for (int i = size == null ? 0 : size.intValue(); i < HashConfig.HASH_POOL_SIZE.getIntValue(); i++) {
				String hash = generateHash(UUID.randomUUID().toString());
				redisTemplate.opsForList().rightPush(HashConfig.HASH_KEY.getStringValue(), hash);
				log.debug("Generated and stored hash: {}", hash);
			}
			log.info("Preloading complete.");
		} else {
			log.info("No need to preload hashes. Redis already has {} hashes.", size);
		}
	}

	@Override
	public String generateUniqueHash() {
		log.info("Generating unique hash...");

		String hash = redisTemplate.opsForList().leftPop(HashConfig.HASH_KEY.getStringValue());
		log.debug("Popped hash from Redis: {}", hash);

		if (hash == null) {
			hash = generateHash(UUID.randomUUID().toString());
			log.debug("Generated new hash as Redis was empty: {}", hash);
		}

		boolean isUnique = !repository.existsByHash(hash);
		log.debug("Hash uniqueness check for {}: {}", hash, isUnique);

		if (!isUnique) {
			redisTemplate.opsForList().rightPush(HashConfig.HASH_KEY.getStringValue(), hash);
			log.warn("Hash {} is not unique, generating a new one.", hash);
			hash = generateHash(UUID.randomUUID().toString());
		}

		Long size = redisTemplate.opsForList().size(HashConfig.HASH_KEY.getStringValue());
		if (size == null || size < HashConfig.HASH_POOL_SIZE.getIntValue()) {
			redisTemplate.opsForList().rightPush(HashConfig.HASH_KEY.getStringValue(), hash);
			log.debug("Added hash back to Redis: {}", hash);
		}

		log.info("Generated unique hash: {}", hash);
		return hash;
	}

	private String generateHash(String input) {
		try {
			MessageDigest digest = MessageDigest.getInstance("SHA-256");
			byte[] hashBytes = digest.digest(input.getBytes(StandardCharsets.UTF_8));
			String hash = Base64.getUrlEncoder().withoutPadding().encodeToString(hashBytes).substring(0, 11);
			log.debug("Generated hash from input {}: {}", input, hash);
			return hash;
		} catch (NoSuchAlgorithmException e) {
			log.error("Error generating hash for input: {}", input, e);
			throw new HashGenerationException(String.format("Error generating hash for input: %s", input));
		}
	}
}
