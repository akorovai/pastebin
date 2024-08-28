package dev.akorovai.hashgenerator.post;

import java.time.LocalDateTime;
import java.util.UUID;

public record PostRequest(
		String language,
		String s3Url,
		UUID userId,
		LocalDateTime expiresAt,
		boolean isPublic
) {}