package dev.akorovai.post.post;

import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "post")
public class Post {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(nullable = false, name = "user_id")
	private UUID userId;

	@Size(max = 255)
	@Column(name = "title")
	private String title;

	@Column(columnDefinition = "TEXT", nullable = false)
	private String content;

	@Size(max = 50)
	@Column(name = "language")
	private String language;

	@CreatedDate
	@Column(nullable = false, updatable = false, name = "created_at", columnDefinition = "TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP")
	private LocalDateTime createdDate;

	@Column(nullable = false, name = "expires_at", columnDefinition = "TIMESTAMP WITH TIME ZONE")
	private LocalDateTime expiresAt;

	@Column(nullable = false, name = "is_public", columnDefinition = "BOOLEAN DEFAULT true")
	private boolean isPublic;

	@Column(name = "view_count", columnDefinition = "INT DEFAULT 0")
	private int viewCount;

	@Column(name = "hash", length = 64)
	private String hash;
}
