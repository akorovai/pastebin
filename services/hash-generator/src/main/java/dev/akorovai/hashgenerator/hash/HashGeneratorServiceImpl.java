package dev.akorovai.hashgenerator.hash;

import dev.akorovai.hashgenerator.post.PostRepository;
import jakarta.annotation.PostConstruct;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
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
public class HashGeneratorServiceImpl implements HashGeneratorService {

	private final RedisTemplate<String, String> redisTemplate;
	private final PostRepository repository;

	@PostConstruct
	public void preloadHashes() {
		Long size = redisTemplate.opsForList().size(HashConfig.HASH_KEY.getStringValue());
		if (size == null || size < HashConfig.HASH_POOL_SIZE.getIntValue()) {
			for (int i = size == null ? 0 : size.intValue(); i < HashConfig.HASH_POOL_SIZE.getIntValue(); i++) {
				redisTemplate.opsForList().rightPush(HashConfig.HASH_KEY.getStringValue(), generateHash(UUID.randomUUID().toString()));
			}
		}
	}

	@Override
	public String generateUniqueHash() {
		String hash = redisTemplate.opsForList().leftPop(HashConfig.HASH_KEY.getStringValue());

		if (hash == null) {
			hash = generateHash(UUID.randomUUID().toString());
		}

		boolean isUnique = !repository.existsByHash(hash);

		if (!isUnique) {

			redisTemplate.opsForList().rightPush(HashConfig.HASH_KEY.getStringValue(), hash);
			hash = generateHash(UUID.randomUUID().toString());
		}


		Long size = redisTemplate.opsForList().size(HashConfig.HASH_KEY.getStringValue());
		if (size == null || size < HashConfig.HASH_POOL_SIZE.getIntValue()) {
			redisTemplate.opsForList().rightPush(HashConfig.HASH_KEY.getStringValue(), hash);
		}

		return hash;
	}

	private String generateHash(String input) {
		try {
			MessageDigest digest = MessageDigest.getInstance("SHA-256");
			byte[] hashBytes = digest.digest(input.getBytes(StandardCharsets.UTF_8));
			return Base64.getUrlEncoder().withoutPadding().encodeToString(hashBytes).substring(0, 11);
		} catch (NoSuchAlgorithmException e) {
			throw new RuntimeException("Error generating hash", e);
		}
	}
}
