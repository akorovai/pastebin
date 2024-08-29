package dev.akorovai.hashgenerator.post;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import org.hibernate.validator.constraints.URL;

import java.time.LocalDateTime;
import java.util.UUID;

public record PostRequest(
		@NotBlank @Size(max = 50)
		String language,

		@NotBlank @URL
		String s3Url,

		@NotNull
		UUID userId,

		@NotNull @Future
		LocalDateTime expiresAt,

		boolean isPublic
) {}
