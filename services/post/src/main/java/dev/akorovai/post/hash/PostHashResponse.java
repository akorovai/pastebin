package dev.akorovai.post.hash;

import java.time.LocalDateTime;
import java.util.UUID;

public record PostHashResponse(UUID userId,
                               String hash,
                               String s3Url,
							   String language,
                               LocalDateTime createdDate,
                               LocalDateTime expiresDate,
                               boolean isPublic) { }