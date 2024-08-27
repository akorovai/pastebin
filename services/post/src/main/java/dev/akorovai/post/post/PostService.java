package dev.akorovai.post.post;

public interface PostService {
	PostResponse createPost( PostRequest request, String userId );

	PostResponse getPost( String id, String userId );
}
