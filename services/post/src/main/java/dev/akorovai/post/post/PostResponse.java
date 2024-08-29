package dev.akorovai.post.post;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
public class PostResponse {

	private UUID userId;
	private String hash;
	private String s3Url;
	private String language;
	private LocalDateTime createdDate;
	private LocalDateTime expiresDate;
}
