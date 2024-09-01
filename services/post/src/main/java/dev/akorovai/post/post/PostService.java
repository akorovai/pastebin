package dev.akorovai.post.post;

import dev.akorovai.post.redis.RatingObject;

import java.util.List;

public interface PostService {
	PostResponse createPost( PostRequest request, String userId );

	PostResponse getPost( String id, String userId );


	List<RatingObject> getRating();
}
