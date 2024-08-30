package dev.akorovai.post.post;

import java.time.LocalDateTime;

public record PostRequest(
		String content,
		String language,
		LocalDateTime expiresAt,
		boolean isPublic
) {}