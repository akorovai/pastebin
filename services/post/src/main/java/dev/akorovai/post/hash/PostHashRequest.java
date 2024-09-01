package dev.akorovai.post.hash;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Builder;

import java.time.LocalDateTime;
import java.util.UUID;


@Builder
public record PostHashRequest (
		@NotBlank @Size(max = 50)
		String language,

		@NotNull
		UUID userId,

		@NotNull @Future
		LocalDateTime expiresAt,

		boolean isPublic
) {}