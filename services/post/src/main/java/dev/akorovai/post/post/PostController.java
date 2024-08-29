package dev.akorovai.post.post;

import dev.akorovai.post.security.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/posts")
public class PostController {

	private final PostService service;
	private final JwtUtil jwtUtil;
	@Autowired
	public PostController(PostService service, JwtUtil jwtUtil) {
		this.service = service;
		this.jwtUtil = jwtUtil;
	}
	@PostMapping
	public ResponseEntity<PostResponse> createPost(
			@RequestBody PostRequest request,
			@RequestHeader("Authorization") String token) {

		String userId = jwtUtil.extractUserId(token);
		PostResponse response = service.createPost(request, userId);
		return ResponseEntity.ok(response);
	}

	@GetMapping("/{id}")
	public ResponseEntity<PostResponse> getPost(
			@PathVariable String id,
			@RequestHeader("Authorization") String token) {

		String userId = jwtUtil.extractUserId(token);
		PostResponse response = service.getPost(id, userId);
		return ResponseEntity.ok(response);
	}
}