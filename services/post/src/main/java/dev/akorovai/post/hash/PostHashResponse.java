package dev.akorovai.post.hash;

import lombok.Builder;

import java.time.LocalDateTime;
import java.util.UUID;
@Builder
public record PostHashResponse(UUID userId,
                               String hash,
							   String language,
                               LocalDateTime createdDate,
                               LocalDateTime expiresDate,
                               boolean isPublic) { }