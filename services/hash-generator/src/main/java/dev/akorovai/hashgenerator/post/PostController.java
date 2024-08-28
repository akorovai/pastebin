package dev.akorovai.hashgenerator.post;


import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/posts")
@RequiredArgsConstructor
public class PostController {

	private final PostService service;


	@PostMapping
	public ResponseEntity<PostResponse> createPostWithUniqueHash( @RequestBody PostRequest postRequest ) {
		var newPost = service.createPost(postRequest);
		return ResponseEntity.ok().body(newPost);
	}

	@GetMapping("/{hash}")
	public ResponseEntity<PostResponse> getPost( @PathVariable String hash ) {
		var post = service.getPost(hash);
		return ResponseEntity.ok().body(post);
	}
}
