package dev.akorovai.post.post;


import java.time.LocalDateTime;
import java.util.UUID;

public record PostHashRequest(
		String language,
		String s3Url,
		UUID userId,
		LocalDateTime expiresAt,
		boolean isPublic
) {}