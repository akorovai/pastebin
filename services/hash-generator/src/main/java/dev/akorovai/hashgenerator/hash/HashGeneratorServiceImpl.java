package dev.akorovai.hashgenerator.hash;

import dev.akorovai.hashgenerator.excepion.HashGenerationException;
import dev.akorovai.hashgenerator.post.PostRepository;
import dev.akorovai.hashgenerator.redis.RedisService;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

@Service
@RequiredArgsConstructor(onConstructor_ = {@Autowired})
@Slf4j
@EnableAsync
public class HashGeneratorServiceImpl implements HashGeneratorService {

	private final PostRepository repository;
	private final RedisService redisService;

	private static final String HASH_KEY = "available_hashes";
	private static final int HASH_POOL_SIZE = 100;
	private static final int HASH_THRESHOLD = 80;

	@PostConstruct
	public void preloadHashes() {
		replenishHashesIfNeeded().join();
	}

	@Override
	public String generateUniqueHash() {
		log.info("Generating unique hash...");

		String hash = redisService.pullValue(HASH_KEY);
		log.debug("Popped hash from Redis: {}", hash);

		if (hash == null) {
			hash = generateHash(UUID.randomUUID().toString());
			log.debug("Generated new hash as Redis was empty: {}", hash);
		}

		if (!isHashUnique(hash)) {
			log.warn("Hash {} is not unique, generating a new one.", hash);
			hash = generateHash(UUID.randomUUID().toString());
		}

		replenishHashesIfNeeded();

		log.info("Generated unique hash: {}", hash);
		return hash;
	}

	@Async
	public CompletableFuture<Void> replenishHashesIfNeeded() {
		int currentSize = getRedisSize();
		int hashesToGenerate = HASH_POOL_SIZE - currentSize;

		if (hashesToGenerate > HASH_THRESHOLD) {
			log.info("Replenishing {} hashes into Redis...", hashesToGenerate);
			for (int i = 0; i < hashesToGenerate; i++) {
				String hash = generateHash(UUID.randomUUID().toString());
				redisService.pushValue(HASH_KEY, hash);
				log.debug("Generated and stored hash: {}", hash);
			}
			log.info("Replenishing complete.");
		} else {
			log.debug("No need to replenish hashes. Redis already has {} hashes.", currentSize);
		}
		return CompletableFuture.completedFuture(null);
	}

	private int getRedisSize() {
		Long size = redisService.getSize(HASH_KEY);
		return size == null ? 0 : size.intValue();
	}

	private boolean isHashUnique(String hash) {
		return !repository.existsByHash(hash);
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