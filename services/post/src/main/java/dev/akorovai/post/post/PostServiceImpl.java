package dev.akorovai.post.post;

import dev.akorovai.post.exception.PostNotFoundException;
import dev.akorovai.post.exception.PostNotPublicException;
import dev.akorovai.post.hash.HashClient;
import dev.akorovai.post.hash.PostHashRequest;
import dev.akorovai.post.hash.PostHashResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@Slf4j
public class PostServiceImpl implements PostService {

	private final HashClient hashClient;

	@Override
	public PostResponse createPost(PostRequest request, String userId) {
		log.info("Creating post for userId: {}", userId);

		PostHashRequest psh = PostHashRequest.builder()
				                      .s3Url("https://your-s3-bucket.s3.amazonaws.com/your-post-file.txt")
				                      .language(request.language())
				                      .userId(UUID.fromString(userId))
				                      .expiresAt(request.expiresAt())
				                      .isPublic(request.isPublic())
				                      .build();

		PostHashResponse newPost = hashClient.createPostWithGeneratedHash(psh)
				                           .orElseThrow(() -> {
					                           log.error("Failed to create post for userId: {}", userId);
					                           return new RuntimeException("Failed to create post");
				                           });

		log.info("Post created successfully with hash: {}", newPost.hash());

		return PostResponse.builder()
				       .userId(UUID.fromString(userId))
				       .hash(newPost.hash())
				       .s3Url("***")  // Assuming this is a placeholder or needs to be replaced with a dynamic value
				       .language(newPost.language())
				       .createdDate(newPost.createdDate())
				       .expiresDate(newPost.expiresDate())
				       .build();
	}

	@Override
	public PostResponse getPost(String id, String userId) {
		log.info("Retrieving post with ID: {} for userId: {}", id, userId);

		PostHashResponse postByHash = hashClient.getPostByHash(id)
				                              .orElseThrow(() -> {
					                              log.error("Post with ID: {} not found", id);
					                              return new PostNotFoundException(id);
				                              });

		if (Boolean.FALSE.equals(postByHash.isPublic())) {
			log.warn("Post with ID: {} is not public. UserId: {}", id, userId);
			throw new PostNotPublicException(String.format(
					"You don't have authority to see the post with ID: %s", id));
		}

		log.info("Post retrieved successfully with hash: {}", postByHash.hash());

		return PostResponse.builder()
				       .userId(UUID.fromString(userId))
				       .hash(postByHash.hash())
				       .s3Url(postByHash.s3Url())
				       .language(postByHash.language())
				       .createdDate(postByHash.createdDate())
				       .expiresDate(postByHash.expiresDate())
				       .build();
	}
}
