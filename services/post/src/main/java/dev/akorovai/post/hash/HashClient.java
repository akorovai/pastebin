package dev.akorovai.post.hash;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.Optional;

@FeignClient(name="hash-generator-service", url="${application.config.hash-generator-url}")
public interface HashClient {
	@GetMapping(value = "/{hash}")
	Optional<PostHashResponse> getPostByHash(@PathVariable("hash") String hash);

	@PostMapping
	Optional<PostHashResponse> createPostWithGeneratedHash(@RequestBody PostHashRequest request);
}
