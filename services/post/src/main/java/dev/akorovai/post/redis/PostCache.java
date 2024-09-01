package dev.akorovai.post.redis;

import lombok.Builder;

import java.time.LocalDateTime;
import java.util.UUID;
@Builder
public record PostCache (UUID userId,
                        String s3Url,
                        String language,
                        LocalDateTime createdDate,
                        LocalDateTime expiresDate,
                         boolean isPublic){ }
