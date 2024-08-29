package dev.akorovai.post.exception;

import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = false)
@Data
public class PostNotPublicException extends RuntimeException {
	private final String message;
}