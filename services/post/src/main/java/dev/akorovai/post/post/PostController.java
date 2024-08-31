package dev.akorovai.post.post;

import dev.akorovai.post.security.JwtUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/posts")
@Tag(name = "Posts", description = "Operations related to posts")
public class PostController {

	private final PostService service;
	private final JwtUtil jwtUtil;

	@Autowired
	public PostController(PostService service, JwtUtil jwtUtil) {
		this.service = service;
		this.jwtUtil = jwtUtil;
	}

	@PostMapping
	@Operation(
			summary = "Create a new post",
			description = "Creates a new post with the provided details.",
			responses = {
					@ApiResponse(
							responseCode = "200",
							description = "Post created successfully",
							content = @Content(schema = @Schema(implementation = PostResponse.class))
					),
					@ApiResponse(
							responseCode = "400",
							description = "Invalid input provided",
							content = @Content(schema = @Schema(hidden = true))
					),
					@ApiResponse(
							responseCode = "401",
							description = "Unauthorized",
							content = @Content(schema = @Schema(hidden = true))
					),
					@ApiResponse(
							responseCode = "413",
							description = "Payload too large",
							content = @Content(schema = @Schema(hidden = true))
					),
					@ApiResponse(
							responseCode = "500",
							description = "Internal server error",
							content = @Content(schema = @Schema(hidden = true))
					)
			},
			security = @SecurityRequirement(name = "BearerAuth")
	)
	public ResponseEntity<PostResponse> createPost(
			@Parameter(description = "Post creation request details") @RequestBody PostRequest request,
			@RequestHeader("Authorization") String token) {

		String userId = jwtUtil.extractUserId(token);
		PostResponse response = service.createPost(request, userId);
		return ResponseEntity.ok(response);
	}

	@GetMapping("/{id}")
	@Operation(
			summary = "Retrieve a post by Hash",
			description = "Retrieves a post by its unique identifier.",
			responses = {
					@ApiResponse(
							responseCode = "200",
							description = "Post retrieved successfully",
							content = @Content(schema = @Schema(implementation = PostResponse.class))
					),
					@ApiResponse(
							responseCode = "401",
							description = "Unauthorized",
							content = @Content(schema = @Schema(hidden = true))
					),
					@ApiResponse(
							responseCode = "404",
							description = "Post not found",
							content = @Content(schema = @Schema(hidden = true))
					),
					@ApiResponse(
							responseCode = "500",
							description = "Internal server error",
							content = @Content(schema = @Schema(hidden = true))
					)
			},
			security = @SecurityRequirement(name = "BearerAuth")
	)
	public ResponseEntity<PostResponse> getPost(
			@Parameter(description = "ID of the post to retrieve") @PathVariable String id,
			@RequestHeader("Authorization") String token) {

		String userId = jwtUtil.extractUserId(token);
		PostResponse response = service.getPost(id, userId);
		return ResponseEntity.ok(response);
	}


}