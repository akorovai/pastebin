package dev.akorovai.post.post;

import java.time.LocalDateTime;

public record PostRequest(
		String title,
		String content,
		String language,
		LocalDateTime expiresAt,
		boolean isPublic
) {}