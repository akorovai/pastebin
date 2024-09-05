package dev.akorovai.post.post;

import dev.akorovai.post.redis.RatingObject;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@Slf4j
@RestController
@RequestMapping("api/posts")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class PostController {

	private final PostService service;

	@PostMapping
	public ResponseEntity<PostResponse> createPost( @RequestBody PostRequest request,
			@RequestHeader(value = "X-User-Id", required = false) String userId) {
		if (userId == null) {
			log.debug("X-User-Id is null");
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
		}
		PostResponse response = service.createPost(request, userId);
		return ResponseEntity.ok(response);
	}

	@GetMapping("/{id}")
	public ResponseEntity<PostResponse> getPost(
			@PathVariable String id,
			@RequestHeader(value = "X-User-Id", required = false) String userId) {
		if (userId == null) {
			log.debug("X-User-Id is null");
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
		}
		PostResponse response = service.getPost(id, userId);
		return ResponseEntity.ok(response);
	}
	@GetMapping("/rating")
	public ResponseEntity<List<RatingObject>> getPostsRating() {
		return ResponseEntity.ok().body(service.getRating());
	}

}