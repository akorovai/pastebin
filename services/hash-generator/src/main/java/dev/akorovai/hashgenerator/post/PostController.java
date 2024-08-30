package dev.akorovai.hashgenerator.post;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/posts")
@RequiredArgsConstructor
@Slf4j
public class PostController {

	private final PostService service;

	@PostMapping
	public ResponseEntity<PostResponse> createPostWithUniqueHash(@Valid @RequestBody PostRequest postRequest) {


		PostResponse newPost = service.createPost(postRequest);

		log.info("Post created with hash: {}", newPost.hashCode());

		return ResponseEntity.ok().body(newPost);
	}

	@GetMapping("/{hash}")
	public ResponseEntity<PostResponse> getPost(@Valid @PathVariable("hash") String hash) {
		log.info("Received request to retrieve post with hash: {}", hash);

		PostResponse post = service.getPost(hash);

		log.info("Post retrieved with hash: {}", hash);

		return ResponseEntity.ok().body(post);
	}
}
