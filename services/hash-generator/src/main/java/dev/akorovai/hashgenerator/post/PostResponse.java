package dev.akorovai.hashgenerator.post;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PostResponse {

	private UUID userId;
	private String hash;
	private String s3Url;
	private String language;
	private LocalDateTime createdDate;
	private LocalDateTime expiresDate;
	private boolean isPublic;
}
