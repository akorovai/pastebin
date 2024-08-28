package dev.akorovai.post.post;

import java.time.LocalDateTime;
import java.util.UUID;

public record PostHashResponse(UUID userId,
                               String hash,
                               String s3Url,
                               LocalDateTime createdAt,
                               LocalDateTime expiresAt) { }