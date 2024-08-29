package dev.akorovai.hashgenerator.post;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;
import org.hibernate.validator.constraints.URL;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "post")
@EntityListeners(AuditingEntityListener.class)
@ToString
public class Post {

	@Id
	@Column(name = "hash", length = 64, unique = true, nullable = false)
	private String hash;

	@NotNull
	@Column(nullable = false, name = "user_id")
	private UUID userId;

	@URL
	@Column(name = "s3_url")
	private String s3Url;

	@Size(max = 50)
	@Column(name = "language")
	private String language;

	@NotNull
	@CreatedDate
	@Column(nullable = false, updatable = false, name = "created_at")
	private LocalDateTime createdDate;

	@NotNull
	@Future
	@Column(nullable = false, name = "expires_at")
	private LocalDateTime expiresDate;

	@Column(nullable = false, name = "is_public", columnDefinition = "BOOLEAN DEFAULT true")
	private boolean isPublic;

	@Min(0)
	@Column(name = "view_count", columnDefinition = "INT DEFAULT 0")
	private int viewCount;
}
