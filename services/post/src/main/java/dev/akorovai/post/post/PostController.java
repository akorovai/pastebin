package dev.akorovai.post.post;

import dev.akorovai.post.security.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/posts")
@RequiredArgsConstructor
public class PostController {

	private final PostService service;
	private final JwtUtil jwtUtil;

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
