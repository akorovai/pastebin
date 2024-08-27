package dev.akorovai.post.post;

import org.springframework.stereotype.Service;

@Service
public class PostServiceImpl implements PostService {
	// TODO Amazon S3 service
	// TODO Hash-service
	@Override
	public PostResponse createPost( PostRequest request, String userId ) {
		return null;
	}

	@Override
	public PostResponse getPost( String id, String userId ) {
		return null;
	}
}
