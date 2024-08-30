package dev.akorovai.post.exception;

import feign.FeignException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class PostExceptionHandler {

	private static final Logger log = LoggerFactory.getLogger(PostExceptionHandler.class);

	@ExceptionHandler(PostNotFoundException.class)
	public ResponseEntity<String> handlePostNotFoundException(PostNotFoundException ex) {
		log.error("Post not found: {}", ex.getMessage());
		return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
	}

	@ExceptionHandler(FeignException.class)
	public ResponseEntity<String> handleFeignException(FeignException ex) {
		HttpStatus status = switch (ex.status()) {
			case 404 -> HttpStatus.NOT_FOUND;
			case 400 -> HttpStatus.BAD_REQUEST;
			default -> HttpStatus.INTERNAL_SERVER_ERROR;
		};
		log.error("Feign exception: {}", ex.getMessage());
		return ResponseEntity.status(status).body(ex.getMessage());
	}

	@ExceptionHandler(JwtTokenException.class)
	public ResponseEntity<String> handleJwtTokenException(JwtTokenException ex) {
		log.error("JWT token exception: {}", ex.getMessage());
		return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(ex.getMessage());
	}

	@ExceptionHandler(FileSizeExceededException.class)
	public ResponseEntity<String> handleFileSizeExceededException(FileSizeExceededException ex) {
		log.error("File size exceeded: {}", ex.getMessage());
		return ResponseEntity.status(HttpStatus.PAYLOAD_TOO_LARGE).body(ex.getMessage());
	}

	@ExceptionHandler(PostCreationFailedException.class)
	public ResponseEntity<String> handlePostCreationFailedException(PostCreationFailedException ex) {
		log.error("Post creation failed: {}", ex.getMessage());
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ex.getMessage());
	}

	@ExceptionHandler(S3StorageException.class)
	public ResponseEntity<String> handleS3StorageException(S3StorageException ex) {
		log.error("S3 storage exception: {}", ex.getMessage());
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ex.getMessage());
	}

	@ExceptionHandler(RuntimeException.class)
	public ResponseEntity<String> handleRuntimeException(RuntimeException ex) {
		log.error("Runtime exception: {}", ex.getMessage(), ex);
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ex.getMessage());
	}
}