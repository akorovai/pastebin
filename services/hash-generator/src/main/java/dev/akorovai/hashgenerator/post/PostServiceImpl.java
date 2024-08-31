package dev.akorovai.hashgenerator.post;

import dev.akorovai.hashgenerator.event.PostViewedEvent;
import dev.akorovai.hashgenerator.excepion.PostNotActiveException;
import dev.akorovai.hashgenerator.excepion.PostNotFoundException;
import dev.akorovai.hashgenerator.hash.HashGeneratorService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class PostServiceImpl implements PostService {

	private final PostRepository repository;
	private final HashGeneratorService hashGeneratorService;
	private final ModelMapper mapper;
	private final ApplicationEventPublisher eventPublisher;

	@Override
	@Transactional
	public PostResponse createPost(PostRequest postRequest) {
		log.info("Creating post with request: {}", postRequest);

		String hash = hashGeneratorService.generateUniqueHash();
		log.debug("Generated unique hash: {}", hash);



		Post post = Post.builder()
				            .hash(hash)
				            .userId(postRequest.userId())
				            .language(postRequest.language())
				            .expiresDate(postRequest.expiresAt())
				            .isPublic(postRequest.isPublic())
				            .build();

		Post savedPost = repository.save(post);
		log.info("Post created and saved: {}", savedPost);

		return mapper.map(savedPost, PostResponse.class);
	}

	@Override
	public PostResponse getPost(String hash) {
		log.info("Fetching post with hash: {}", hash);

		Post post = repository.findByHash(hash)
				            .orElseThrow(() -> {
					            log.error("Post with hash {} not found", hash);
					            return new PostNotFoundException("Post with hash " + hash + " not found");
				            });
		if(!post.isActive()) {
			throw new PostNotActiveException(String.format("Post:%s is not active", post.getHash()));
		}

		eventPublisher.publishEvent(new PostViewedEvent(hash));
		log.info("Post with hash {} is public and has been viewed", hash);

		return mapper.map(post, PostResponse.class);
	}
}