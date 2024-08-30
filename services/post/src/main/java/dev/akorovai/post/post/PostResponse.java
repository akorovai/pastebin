package dev.akorovai.post.post;

import lombok.Builder;

import java.time.LocalDateTime;
import java.util.UUID;


@Builder
public record PostResponse(UUID userId,
                           String hash,
                           String s3Url,
                           String language,
                           LocalDateTime createdDate,
                           LocalDateTime expiresDate) { }
