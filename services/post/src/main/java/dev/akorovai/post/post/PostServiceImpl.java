package dev.akorovai.post.post;

import dev.akorovai.post.aws.InMemoryMultipartFile;
import dev.akorovai.post.aws.S3PresignedUrlGenerator;
import dev.akorovai.post.aws.StorageService;
import dev.akorovai.post.exception.*;
import dev.akorovai.post.hash.HashClient;
import dev.akorovai.post.hash.PostHashRequest;
import dev.akorovai.post.hash.PostHashResponse;
import dev.akorovai.post.redis.PostCache;
import dev.akorovai.post.redis.PostCacheService;
import dev.akorovai.post.redis.RatingObject;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.services.s3.model.NoSuchBucketException;
import software.amazon.awssdk.services.s3.model.NoSuchKeyException;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@Slf4j
public class PostServiceImpl implements PostService {

	private static final long MAX_FILE_SIZE = 10 * 1024 * 1024;

	private final HashClient hashClient;
	private final StorageService storageService;
	private final S3PresignedUrlGenerator s3PresignedUrlGenerator;
	private final PostCacheService cacheService;

	@Value("${io.reflectoring.aws.s3.bucketName}")
	private String bucketName;

	@Override
	public PostResponse createPost(PostRequest request, String userId) {
		log.info("Creating post for userId: {}", userId);

		byte[] contentBytes = request.content().getBytes(StandardCharsets.UTF_8);
		if (contentBytes.length > MAX_FILE_SIZE) {
			log.error("File size exceeds the limit of 10 MB");
			throw new FileSizeExceededException("File size exceeds the limit of 10 MB");
		}

		PostHashRequest psh = PostHashRequest.builder()
				                      .language(request.language())
				                      .userId(UUID.fromString(userId))
				                      .expiresAt(request.expiresAt())
				                      .isPublic(request.isPublic())
				                      .build();

		PostHashResponse newPost = hashClient.createPostWithGeneratedHash(psh).orElseThrow(() -> {
			log.error("Failed to create post for userId: {}", userId);
			return new PostCreationFailedException("Failed to create post");
		});

		String hash = newPost.hash();
		log.info("Generated hash: {}", hash);

		String presignedUrl = s3PresignedUrlGenerator.generatePresignedUrl(bucketName, hash + ".txt");

		MultipartFile file = new InMemoryMultipartFile(bucketName, hash + ".txt", "text/plain", contentBytes);

		try {
			storageService.save(file);
			PostResponse postResponse = PostResponse.builder()
					                            .userId(UUID.fromString(userId))
					                            .hash(newPost.hash())
					                            .s3Url(presignedUrl)
					                            .language(newPost.language())
					                            .createdDate(newPost.createdDate())
					                            .expiresDate(newPost.expiresDate())
					                            .build();

			cacheService.savePost(postResponse, request.isPublic());

			log.info("Post created successfully with hash: {}", hash);

			return postResponse;
		} catch (IOException e) {
			log.error("Error saving file to S3", e);
			throw new S3StorageException("Failed to save file to S3");
		} catch (NoSuchBucketException e) {
			log.error("The specified S3 bucket does not exist: {}", bucketName, e);
			throw new S3StorageException("The specified S3 bucket does not exist");
		}
	}

	@Override
	public PostResponse getPost(String hash, String userId) {
		log.info("Retrieving post with ID: {} for userId: {}", hash, userId);

		PostCache cache = cacheService.getPost(hash);
		PostHashResponse postByHash;

		if (cache == null) {
			postByHash = hashClient.getPostByHash(hash).orElseThrow(() -> {
				log.error("Post with ID: {} not found", hash);
				return new PostNotFoundException(hash);
			});
		} else {
			postByHash = PostHashResponse.builder()
					             .userId(cache.userId())
					             .hash(hash)
					             .language(cache.language())
					             .createdDate(cache.createdDate())
					             .expiresDate(cache.expiresDate())
					             .isPublic(cache.isPublic())
					             .build();
		}

		if (!userId.equals(postByHash.userId().toString()) && Boolean.FALSE.equals(postByHash.isPublic())) {
			log.warn("Post with ID: {} is not public and user is not the owner. UserId: {}", hash, userId);
			throw new PostNotPublicException(String.format("You don't have authority to see the post with ID: %s", hash));
		}

		try {
			storageService.retrieve(hash + ".txt");
			log.info("Post retrieved successfully with hash: {}", postByHash.hash());

			String presignedUrl = s3PresignedUrlGenerator.generatePresignedUrl(bucketName, hash + ".txt");

			return PostResponse.builder()
					       .userId(UUID.fromString(userId))
					       .hash(postByHash.hash())
					       .s3Url(presignedUrl)
					       .language(postByHash.language())
					       .createdDate(postByHash.createdDate())
					       .expiresDate(postByHash.expiresDate())
					       .build();
		} catch (NoSuchKeyException e) {
			log.error("The specified key does not exist in S3: {}", hash, e);
			throw new PostNotFoundException("Post with ID: " + hash + " not found in S3");
		} catch (NoSuchBucketException e) {
			log.error("The specified S3 bucket does not exist: {}", bucketName, e);
			throw new S3StorageException("The specified S3 bucket does not exist");
		}
	}

	@Override
	public List<RatingObject> getRating() {
		log.info("Retrieving rating objects");
		return cacheService.getMostPopularPosts().stream().toList();
	}
}