package dev.akorovai.hashgenerator.post;

public interface PostService {
	PostResponse createPost( PostRequest postRequest );

	PostResponse getPost( String hash );
}
